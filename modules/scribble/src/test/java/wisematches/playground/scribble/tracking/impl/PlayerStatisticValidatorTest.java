package wisematches.playground.scribble.tracking.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.scribble.PlayerStatisticValidator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Ignore
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-config.xml"
})
public class PlayerStatisticValidatorTest {
	@Autowired
	private PlayerStatisticValidator statisticValidator;

	@Autowired
	private PlatformTransactionManager transactionManager;

	public PlayerStatisticValidatorTest() {
	}

	@Test
	public void recalculateStatistics() {
		TransactionTemplate tt = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		tt.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					statisticValidator.recalculateStatistics();
				} catch (BoardLoadingException e) {
					e.printStackTrace();
					throw new IllegalStateException("Recalculation failed", e);
				}
			}
		});
	}

	@Test
	public void recalculateWinnersAndRatings() {
		TransactionTemplate tt = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		tt.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					statisticValidator.recalculateWinnersAndRatings();
				} catch (BoardLoadingException e) {
					e.printStackTrace();
					throw new IllegalStateException("Recalculation failed", e);
				}
			}
		});
	}
}