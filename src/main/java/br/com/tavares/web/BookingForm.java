package br.com.tavares.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookingForm {
  @NotNull
  private Long serviceId;

  @NotBlank
  private String customerName;

  @NotBlank
  private String phone;

  @NotBlank
  private String date;

  @NotBlank
  private String time;

  private boolean levaTraz;
  private String pickupAddress;
  private String notes;

  public Long getServiceId() { return serviceId; }
  public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
  public String getCustomerName() { return customerName; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }
  public String getTime() { return time; }
  public void setTime(String time) { this.time = time; }
  public boolean isLevaTraz() { return levaTraz; }
  public void setLevaTraz(boolean levaTraz) { this.levaTraz = levaTraz; }
  public String getPickupAddress() { return pickupAddress; }
  public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
  public String getNotes() { return notes; }
  public void setNotes(String notes) { this.notes = notes; }
}
