package br.com.tavares.domain;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "blocked_times")
public class BlockedTime {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="start_at", nullable = false)
  private ZonedDateTime startAt;

  @Column(name="end_at", nullable = false)
  private ZonedDateTime endAt;

  @Column(nullable = false)
  private String reason;

  public BlockedTime() {}

  public BlockedTime(ZonedDateTime startAt, ZonedDateTime endAt, String reason) {
    this.startAt = startAt;
    this.endAt = endAt;
    this.reason = reason;
  }

  public Long getId() { return id; }
  public ZonedDateTime getStartAt() { return startAt; }
  public void setStartAt(ZonedDateTime startAt) { this.startAt = startAt; }
  public ZonedDateTime getEndAt() { return endAt; }
  public void setEndAt(ZonedDateTime endAt) { this.endAt = endAt; }
  public String getReason() { return reason; }
  public void setReason(String reason) { this.reason = reason; }
}
