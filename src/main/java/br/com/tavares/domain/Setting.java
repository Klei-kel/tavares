package br.com.tavares.domain;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "settings")
public class Setting {
  @Id
  private Long id = 1L;

  @Column(nullable = false)
  private ZonedDateTime releasedUntil;

  public Setting() {}

  public Setting(ZonedDateTime releasedUntil) {
    this.releasedUntil = releasedUntil;
  }

  public Long getId() { return id; }
  public ZonedDateTime getReleasedUntil() { return releasedUntil; }
  public void setReleasedUntil(ZonedDateTime releasedUntil) { this.releasedUntil = releasedUntil; }
}
