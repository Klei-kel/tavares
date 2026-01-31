package br.com.tavares.repo;

import br.com.tavares.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  @Query("select a from Appointment a where a.status <> 'CANCELED' and a.startAt >= :from and a.startAt < :to order by a.startAt asc")
  List<Appointment> findActiveBetween(ZonedDateTime from, ZonedDateTime to);

  boolean existsByStartAtAndStatusNot(ZonedDateTime startAt, Appointment.Status status);
}
