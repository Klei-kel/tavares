package br.com.tavares.web;

import br.com.tavares.domain.Appointment;
import br.com.tavares.repo.AppointmentRepository;
import br.com.tavares.repo.ServiceRepository;
import br.com.tavares.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.*;

@Controller
public class PublicController {

  private final ServiceRepository serviceRepo;
  private final AppointmentRepository appointmentRepo;
  private final ScheduleService scheduleService;
  private final ZoneId zoneId;

  @Value("${app.business.whatsapp:(11) 94186-7850}")
  private String whatsapp;

  public PublicController(ServiceRepository serviceRepo, AppointmentRepository appointmentRepo, ScheduleService scheduleService,
                          @Value("${app.timezone:America/Sao_Paulo}") String tz) {
    this.serviceRepo = serviceRepo;
    this.appointmentRepo = appointmentRepo;
    this.scheduleService = scheduleService;
    this.zoneId = ZoneId.of(tz);
  }

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("brandName", "Tavares Lava Rápido");
    model.addAttribute("address", "Estr. Canal do Cocaia, 2605");
    model.addAttribute("whatsapp", whatsapp);

    model.addAttribute("services", serviceRepo.findByActiveTrueOrderByNameAsc());
    model.addAttribute("days", scheduleService.availableSlotsForReleasedWeek(zoneId));
    model.addAttribute("form", new BookingForm());
    model.addAttribute("releasedUntil", scheduleService.getOrCreateSetting(zoneId).getReleasedUntil());
    return "index";
  }

  @PostMapping("/agendar")
  @Transactional
  public String book(@Valid @ModelAttribute("form") BookingForm form, BindingResult br) {
    if (br.hasErrors()) {
      return "redirect:/?err=preencha-os-campos";
    }

    var service = serviceRepo.findById(form.getServiceId()).orElseThrow();
    LocalDate date = LocalDate.parse(form.getDate());
    LocalTime time = LocalTime.parse(form.getTime());
    ZonedDateTime start = ZonedDateTime.of(date, time, zoneId);
    ZonedDateTime end = start.plusMinutes(ScheduleService.SLOT_MINUTES);

    var setting = scheduleService.getOrCreateSetting(zoneId);
    if (start.isAfter(setting.getReleasedUntil())) return "redirect:/?err=semana-nao-liberada";
    if (!scheduleService.isOpenDay(date)) return "redirect:/?err=dia-fechado";

    scheduleService.assertSlotAvailableOrThrow(start);

    Appointment a = new Appointment();
    a.setCustomerName(form.getCustomerName().trim());
    a.setPhone(form.getPhone().trim());
    a.setService(service);
    a.setStartAt(start);
    a.setEndAt(end);
    a.setLevaTraz(form.isLevaTraz());
    a.setPickupAddress(form.isLevaTraz() ? safe(form.getPickupAddress()) : null);
    a.setNotes(safe(form.getNotes()));
    a.setStatus(Appointment.Status.CONFIRMED);

    a = appointmentRepo.save(a);
    return "redirect:/sucesso?id=" + a.getId();
  }

  @GetMapping("/sucesso")
  public String success(@RequestParam("id") Long id, Model model) {
    Appointment a = appointmentRepo.findById(id).orElseThrow();
    model.addAttribute("appt", a);
    model.addAttribute("whatsapp", whatsapp);
    return "success";
  }

  private static String safe(String s){ return (s == null) ? "" : s.trim(); }
}
