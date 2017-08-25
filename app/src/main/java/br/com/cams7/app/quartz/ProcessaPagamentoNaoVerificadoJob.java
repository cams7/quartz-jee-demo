package br.com.cams7.app.quartz;

import java.util.List;
import java.util.logging.Level;

import org.quartz.DisallowConcurrentExecution;
//import org.quartz.ExecuteInJTATransaction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import br.com.cams7.app.model.entity.Pedido;

/**
 * @author cesaram
 *
 *         Processa os pagamento à vista
 */
@DisallowConcurrentExecution
// @ExecuteInJTATransaction
public class ProcessaPagamentoNaoVerificadoJob extends ProcessaPagamento implements Job {

	public static String JOB_GROUP = "pagamento-nao-verificado";
	public static JobKey PAGAMENTO_NAO_VERIFICADO = JobKey.jobKey(JOB_GROUP + "-job", JOB_GROUP);

	public static String PAGAMENTOS_NAO_VERIFICADOS = "PagamentosPendentes";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Scheduler scheduler = context.getScheduler();

			if (scheduler.checkExists(CarregaPedidosNaoVerificadosJob.CARREGA_PEDIDOS_NAO_VERIFICADOS)) {

				List<Long> pedidos = getPagamentos().get(PAGAMENTOS_NAO_VERIFICADOS);
				System.out.println("Processa: " + pedidos);

				if (pedidos != null && !pedidos.isEmpty()) {
					Long pedidoId = pedidos.remove(0);
					Pedido pedido = getPedidoRepository().buscaPeloId(pedidoId);
					processaPagamento(pedido);
				}
			}

			LOG.log(Level.INFO, "Trigger: {0}, Fired at: {1}, Instance: {2}",
					new Object[] { context.getTrigger().getKey(), SDF.format(context.getFireTime()),
							context.getScheduler().getSchedulerInstanceId() });
		} catch (SchedulerException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}

	}

}
