package org.camunda.bpm.getstarted.pizza;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.getstarted.pizza.interceptors.Logged;

public class SendRejectionEmail implements JavaDelegate {

	@Override
	@Logged
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Sending rejection email...");
	}

}
