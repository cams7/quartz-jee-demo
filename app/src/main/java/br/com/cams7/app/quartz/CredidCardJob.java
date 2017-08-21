package br.com.cams7.app.quartz;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.ExecuteInJTATransaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import br.com.cams7.app.model.OrderRepository;
import br.com.cams7.app.model.entity.Order;
import br.com.cams7.app.model.entity.Order.PaymentMethod;

/**
 * @author cesaram
 *
 *         Processa os pagamento no cartão de crédito
 */
@DisallowConcurrentExecution
@ExecuteInJTATransaction
public class CredidCardJob implements Job {

	private static final Logger LOG = Logger.getLogger(CredidCardJob.class.getSimpleName());
	private final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");

	@EJB
	private OrderRepository orderRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			LOG.info(String.format("Trigger: %s, Fired at: %s, Instance: %s", context.getTrigger().getKey(),
					SDF.format(context.getFireTime()), context.getScheduler().getSchedulerInstanceId()));
		} catch (SchedulerException e) {
		}

		Order order = orderRepository.findByPaymentMethod(PaymentMethod.CREDIT_CARD);
		if (order != null)
			LOG.info(String.format("O pedido (%s) com pagamento no cartão de credito foi processado...",
					SDF.format(order.getOrderDate())));

	}
}