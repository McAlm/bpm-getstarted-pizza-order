package org.camunda.bpm.getstarted.pizza;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.camunda.bpm.engine.cdi.BusinessProcess;

@Named
@ConversationScoped
public class ApproveOrderController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrderEntity orderEntity;

	@Inject
	private OrderBusinessLogic obl;

	@Inject
	private BusinessProcess bp;

	public OrderEntity getOrderEntity() {
		if (orderEntity == null) {
			orderEntity = this.obl.getOrder((Long) bp.getVariable("orderId"));
		}
		return orderEntity;
	}

	public void submitForm() {
		this.obl.mergeOrderAndCompleteTask(this.orderEntity);
	}
}