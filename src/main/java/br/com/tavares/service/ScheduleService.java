package br.com.tavares.service;

import br.com.tavares.domain.Appointment;
import br.com.tavares.domain.BlockedTime;
import br.com.tavares.domain.Setting;
import br.com.tavares.repo.AppointmentRepository;
import br.com.tavares.repo.BlockedTimeRepository;
import br.com.tavares.repo.SettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

  private final AppointmentRepository appointmentRepo;
  private final BlockedTimeRepository blockedRepo;
  private final SettingRepository settingRepo;

  public static final int SLOT_MINUTES = 60;

  public ScheduleService(AppointmentRepository appointmentRepo, BlockedTimeRepository blockedRepo, SettingRepository settingRepo) {
    this.appointmentRepo = appointmentRepo;
    this.blockedRepo = blockedRepo;
    this.settingRepo = settingRepo;
  }

  public Setting getOrCreateSetting(ZoneId zoneId) {
    return settingRepo.findById(1L).orElseGet(() -> {
      ZonedDateTime now = ZonedDateTime.now(zoneId);
      ZonedDateTime endOfWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
          .withHour(23).withMinute(59).withSecond(59).withNano(0);
      Setting s = new Setting(endOfWeek);
      return settingRepo.save(s);
    });
  }

  public ZonedDateTime releaseNextWeek(ZoneId zoneId) {
    Setting s = getOrCreateSetting(zoneId);
    ZonedDateTime next = s.getReleasedUntil().plusWeeks(1);
    s.setReleasedUntil(next);
    settingRepo.save(s);
    return next;
  }

  public record DaySlots(LocalDate date, List<LocalTime> times) {}

  public List<DaySlots> availableSlotsForReleasedWeek(ZoneId zoneId) {
    Setting s = getOrCreateSetting(zoneId);
    ZonedDateTime until = s.getReleasedUntil();

    LocalDate startDate = LocalDate.now(zoneId);
    LocalDate endDate = until.withZoneSameInstant(zoneId).toLocalDate();
    LocalDate cap = startDate.plusDays(6);
    if (endDate.isAfter(cap)) endDate = cap;

    List<DaySlots> out = new ArrayList<>();
    for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
      if (!isOpenDay(d)) continue;
      out.add(new DaySlots(d, availableTimesForDay(d, zoneId, until)));
    }
    return out;
  }

  public List<LocalTime> availableTimesForDay(LocalDate date, ZoneId zoneId, ZonedDateTime releasedUntil) {
    if (!isOpenDay(date)) return List.of();

    LocalTime open = LocalTime.of(8,0);
    LocalTime close = (date.getDayOfWeek() == DayOfWeek.SUNDAY) ? LocalTime.of(14,30) : LocalTime.of(17,30);
    LocalTime lastStart = close.minusMinutes(SLOT_MINUTES);

    List<LocalTime> candidates = new ArrayList<>();
    for (LocalTime t = open; !t.isAfter(lastStart); t = t.plusMinutes(SLOT_MINUTES)) {
      candidates.add(t);
    }

    ZonedDateTime dayStart = date.atStartOfDay(zoneId);
    ZonedDateTime dayEnd = date.plusDays(1).atStartOfDay(zoneId);

    var booked = appointmentRepo.findActiveBetween(dayStart, dayEnd).stream()
        .map(a -> a.getStartAt().withZoneSameInstant(zoneId).toLocalTime())
        .collect(Collectors.toSet());

    List<BlockedTime> blocks = blockedRepo.findOverlapping(dayStart, dayEnd);

    return candidates.stream()
        .filter(t -> {
          ZonedDateTime start = ZonedDateTime.of(date, t, zoneId);
          ZonedDateTime end = start.plusMinutes(SLOT_MINUTES);

          if (start.isAfter(releasedUntil)) return false;
          if (booked.contains(t)) return false;

          for (BlockedTime b : blocks) {
            ZonedDateTime bs = b.getStartAt().withZoneSameInstant(zoneId);
            ZonedDateTime be = b.getEndAt().withZoneSameInstant(zoneId);
            boolean overlap = start.isBefore(be) && end.isAfter(bs);
            if (overlap) return false;
          }
          return true;
        })
        .toList();
  }

  public boolean isOpenDay(LocalDate date) {
    return date.getDayOfWeek() != DayOfWeek.MONDAY;
  }

  @Transactional
  public void assertSlotAvailableOrThrow(ZonedDateTime start) {
    if (appointmentRepo.existsByStartAtAndStatusNot(start, Appointment.Status.CANCELED)) {
      throw new IllegalStateException("Horário indisponível. Atualize a página e escolha outro.");
    }
  }
}
