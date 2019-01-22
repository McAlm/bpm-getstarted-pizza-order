package org.camunda.bpm.getstarted.pizza;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.getstarted.pizza.interceptors.Logged;

@Stateless
@Named("pizzaOrderLogic")
public class OrderBusinessLogic {

	// Inject the entity manager
	@PersistenceContext
	private EntityManager entityManager;

	@Inject
	private TaskForm taskForm;

	private static Logger LOGGER = Logger.getLogger(OrderBusinessLogic.class.getName());

	@Logged
	public void persistOrder(DelegateExecution delegateExecution) {
		// Create new order instance
		OrderEntity orderEntity = new OrderEntity();

		// Get all process variables
		Map<String, Object> variables = delegateExecution.getVariables();

		// Set order attributes
		orderEntity.setCustomer((String) variables.get("customer"));
		orderEntity.setAddress((String) variables.get("address"));
		orderEntity.setPizza((String) variables.get("pizza"));

		/*
		 * Persist order instance and flush. After the flush the id of the order
		 * instance is set.
		 */
		entityManager.persist(orderEntity);
		entityManager.flush();

		// Remove no longer needed process variables
		delegateExecution.removeVariables(variables.keySet());

		// Add newly created order id as process variable
		delegateExecution.setVariable("orderId", orderEntity.getId());
	}

	public OrderEntity getOrder(Long key) {
		return this.entityManager.find(OrderEntity.class, key);
	}

	@Logged
	public void mergeOrderAndCompleteTask(OrderEntity order) {

		this.entityManager.merge(order);

		try {
			this.taskForm.completeTask();
		} catch (IOException e) {
			// Rollback both transactions on error
			throw new RuntimeException("Cannot complete task", e);
		}

	}

	@Logged
	public void rejectOrder(String orderId) {
		Long id = Long.valueOf(orderId);

		OrderEntity order = getOrder(id);
		LOGGER.log(Level.INFO, "\n\n\nSending Email:\nDear {0}, your order {1} of a {2} pizza has been rejected.\n\n\n",
				new String[] { order.getCustomer(), String.valueOf(order.getId()), order.getPizza() });

	}

}