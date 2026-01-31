package br.com.tavares.domain;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "appointments", uniqueConstraints = {
    @UniqueConstraint(name="uk_appointment_start", columnNames = {"start_at"})
})
public class Appointment {
  public enum Status { PENDING, CONFIRMED, CANCELED, DONE }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="customer_name", nullable = false)
  private String customerName;

  @Column(nullable = false)
  private String phone;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name="service_id")
  private ServiceEntity service;

  @Column(name="start_at", nullable = false)
  private ZonedDateTime startAt;

  @Column(name="end_at", nullable = false)
  private ZonedDateTime endAt;

  @Column(name="leva_traz", nullable = false)
  private boolean levaTraz;

  @Column(name="pickup_address")
  private String pickupAddress;

  @Column(name="notes", length = 1000)
  private String notes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Status status = Status.CONFIRMED;

  @Column(name="google_event_id")
  private String googleEventId;

  public Appointment() {}

  public Long getId() { return id; }
  public String getCustomerName() { return customerName; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public ServiceEntity getService() { return service; }
  public void setService(ServiceEntity service) { this.service = service; }
  public ZonedDateTime getStartAt() { return startAt; }
  public void setStartAt(ZonedDateTime startAt) { this.startAt = startAt; }
  public ZonedDateTime getEndAt() { return endAt; }
  public void setEndAt(ZonedDateTime endAt) { this.endAt = endAt; }
  public boolean isLevaTraz() { return levaTraz; }
  public void setLevaTraz(boolean levaTraz) { this.levaTraz = levaTraz; }
  public String getPickupAddress() { return pickupAddress; }
  public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }
  public String getGoogleEventId() { return googleEventId; }
  public void setGoogleEventId(String googleEventId) { this.googleEventId = googleEventId; }
}
