package wisematches.database.concurrent;

import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Wrapper under {@code TaskExecutor} spring interface that process any work inside Hibernate
 * transaction with {@code PROPAGATION_REQUIRES_NEW} propagation.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TransactionAwareTaskExecutor implements TaskExecutor {
	private TaskExecutor taskExecutor;
	private PlatformTransactionManager transactionManager;

	private TransactionTemplate transactionTemplate;

	public TransactionAwareTaskExecutor() {
	}

	@Override
	public void execute(final Runnable task) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						task.run();
					}
				});
			}
		});
	}

	private TransactionTemplate getTransactionTemplate() {
		if (transactionTemplate == null) {
			transactionTemplate = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		}
		return transactionTemplate;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
