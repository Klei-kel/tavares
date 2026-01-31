package br.com.tavares.web;

import br.com.tavares.domain.Appointment;
import br.com.tavares.repo.AppointmentRepository;
import br.com.tavares.service.ScheduleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final AppointmentRepository appointmentRepo;
  private final ScheduleService scheduleService;
  private final ZoneId zoneId;

  public AdminController(AppointmentRepository appointmentRepo, ScheduleService scheduleService,
                         @Value("${app.timezone:America/Sao_Paulo}") String tz) {
    this.appointmentRepo = appointmentRepo;
    this.scheduleService = scheduleService;
    this.zoneId = ZoneId.of(tz);
  }

  @GetMapping("/agenda")
  public String agenda(@RequestParam(value="date", required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                       Model model) {
    LocalDate base = (date == null) ? LocalDate.now(zoneId) : date;
    ZonedDateTime from = base.atStartOfDay(zoneId);
    ZonedDateTime to = base.plusDays(7).atStartOfDay(zoneId);

    model.addAttribute("appointments", appointmentRepo.findActiveBetween(from, to));
    model.addAttribute("releasedUntil", scheduleService.getOrCreateSetting(zoneId).getReleasedUntil());
    return "admin-agenda";
  }

  @PostMapping("/release-next-week")
  public String releaseNextWeek() {
    scheduleService.releaseNextWeek(zoneId);
    return "redirect:/admin/agenda";
  }

  @PostMapping("/cancel/{id}")
  public String cancel(@PathVariable Long id) {
    Appointment a = appointmentRepo.findById(id).orElseThrow();
    a.setStatus(Appointment.Status.CANCELED);
    appointmentRepo.save(a);
    return "redirect:/admin/agenda";
  }
}
