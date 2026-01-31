package br.com.tavares.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "services")
public class ServiceEntity {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(nullable = false)
  private boolean active = true;

  public ServiceEntity() {}

  public ServiceEntity(String name, BigDecimal price) {
    this.name = name;
    this.price = price;
    this.active = true;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }
  public boolean isActive() { return active; }
  public void setActive(boolean active) { this.active = active; }
}
