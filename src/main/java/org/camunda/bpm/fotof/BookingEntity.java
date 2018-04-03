package org.camunda.bpm.fotof;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;

@Entity
public class BookingEntity implements Serializable {

  private static  final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  protected Long id;

  @Version
  protected long version;

  protected String customer;
  protected String address;
  protected String date;
  protected boolean photoshootDone;
  protected boolean inStudio;
  protected boolean orderRecieved;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
  
  public String getDate() {
	  return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public boolean isPhotoshootDone() {
    return photoshootDone;
  }

  public void setPhotoshootDone(boolean photoshootDone) {
    this.photoshootDone = photoshootDone;
  }
  
  public boolean isInStudio() {
	    return inStudio;
  }

  public void setInStudio(boolean inStudio) {
    this.inStudio = inStudio;
  }
  
  public boolean isOrderRecieved() {
	    return orderRecieved;
  }
  
  public void setOrderRecieved(boolean orderRecieved) {
	  this.orderRecieved = orderRecieved;
  }
}