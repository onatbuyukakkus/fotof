package org.camunda.bpm.fotof;

import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Named
public class BookingBusinessLogic {

  // Inject the entity manager
  @PersistenceContext
  private EntityManager entityManager;
  
  //Inject task form available through the Camunda cdi artifact
  @Inject
  private TaskForm taskForm;
  
  private static Logger LOGGER = Logger.getLogger(BookingBusinessLogic.class.getName());

  public void handleBooking(DelegateExecution delegateExecution) {
    // Create new order instance
    BookingEntity bookingEntity = new BookingEntity();

    // Get all process variables
    Map<String, Object> variables = delegateExecution.getVariables();

    // Set order attributes
    bookingEntity.setCustomer((String) variables.get("customer"));
    bookingEntity.setAddress((String) variables.get("address"));
    bookingEntity.setDate((String) variables.get("date"));
    bookingEntity.setInStudio(bookingEntity.getAddress().equals("Studio"));
    
    // Persist order instance and flush. After the flush the
    // id of the order instance is set.
    entityManager.persist(bookingEntity);
    entityManager.flush();

    // Remove no longer needed process variables
    delegateExecution.removeVariables(variables.keySet());

    // Add newly created order id as process variable
    delegateExecution.setVariable("bookingId", bookingEntity.getId());
  }
  
  public BookingEntity getBooking(Long bookingId) {
	  // Load order entity from database
	  return entityManager.find(BookingEntity.class, bookingId);
  }
  
  /*
	Merge updated order entity and complete task form in one transaction. This ensures
	that both changes will rollback if an error occurs during transaction.
   */
  public void mergeBookingAndCompleteTask(BookingEntity bookingEntity) {
	  // Merge detached order entity with current persisted state
	  entityManager.merge(bookingEntity);
	  try {
		  // Complete user task from
		  taskForm.completeTask();
	  } catch (IOException e) {
		  // Rollback both transactions on error
		  throw new RuntimeException("Cannot complete task", e);
	  }
  }
  
  public void rejectBooking(DelegateExecution delegateExecution) {
	  BookingEntity booking = getBooking((Long) delegateExecution.getVariable("bookingId"));
	  LOGGER.log(Level.INFO, "\n\n\nSending Email:\nDear " + booking.getCustomer() + ", you haven't showed up for photoshoot at " + booking.getAddress() + " and " + booking.getDate() + ".\n\n\n");
  }
  
  public void galleryReady(DelegateExecution delegateExecution) {
	  BookingEntity booking = getBooking((Long) delegateExecution.getVariable("bookingId"));
	  LOGGER.log(Level.INFO, "\n\n\nSending Email:\nDear " + booking.getCustomer() + ", your gallery for photoshoot at " + booking.getAddress() + " and " + booking.getDate() + " is ready.\n\n\n");
  }
  
}
