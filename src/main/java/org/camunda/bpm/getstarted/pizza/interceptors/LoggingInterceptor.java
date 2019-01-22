package org.camunda.bpm.getstarted.pizza.interceptors;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Logged
@Interceptor
public class LoggingInterceptor implements Serializable {

	@AroundInvoke
	public Object invokeLogging(InvocationContext invocationContext) throws Exception {
		System.out.println("Entering method:" + invocationContext.getMethod().getName() + " in class "
				+ invocationContext.getMethod().getDeclaringClass().getName());

		return invocationContext.proceed();
	}
}
