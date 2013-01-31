package wisematches.playground.scribble.tracking.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
//@Ignore
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-junit-config.xml"
})
public class PlayerStatisticValidatorTest {
	/*
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
					statisticValidator.recalculateStatistics();
				}
			});
		}
	*/
	@Test
	public void commented() {
		// TODO: commented
//		throw new UnsupportedOperationException("Commented");
	}
}
