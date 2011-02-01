package wisematches.server.deprecated.web.server;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TransactionalUrlHandlerMappingTest {
	@Test
	public void test_isTransactionalBean() {
		final TransactionalUrlHandlerMapping mapping = new TransactionalUrlHandlerMapping();
		assertFalse(mapping.isTransactionalBean(new Bean1()));
		assertTrue(mapping.isTransactionalBean(new Bean2()));
		assertTrue(mapping.isTransactionalBean(new Bean3()));
		assertTrue(mapping.isTransactionalBean(new Bean4()));
	}

	@Test
	public void test_isBeanSupported() {
		final ApplicationContext context = createStrictMock(ApplicationContext.class);
		expect(context.getBeanNamesForType(Object.class)).andReturn(new String[0]);
		replay(context);

		final TransactionalUrlHandlerMapping mapping = new TransactionalUrlHandlerMapping();
		mapping.setApplicationContext(context);

		// Both types supported: no context invocations
		reset(context);
		mapping.setAllowedType(TransactionalUrlHandlerMapping.AllowedType.TRANSACTIONAL_AND_NOT_TRANSACTIONAL);
		replay(context);
		assertTrue(mapping.isBeanSupported("bean1"));
		verify(context);

		// Both types unsupported: no context invocations
		reset(context);
		mapping.setAllowedType(TransactionalUrlHandlerMapping.AllowedType.NONE);
		replay(context);
		assertFalse(mapping.isBeanSupported("bean1"));
		verify(context);

		// Only not transactional supported
		reset(context);
		expect(context.getBean("bean1")).andReturn(new Bean1());
		expect(context.getBean("bean2")).andReturn(new Bean2());
		mapping.setAllowedType(TransactionalUrlHandlerMapping.AllowedType.NOT_TRANSACTION_ONLY);
		replay(context);
		assertTrue(mapping.isBeanSupported("bean1"));
		assertFalse(mapping.isBeanSupported("bean2"));
		verify(context);

		// Only transactional supported
		reset(context);
		expect(context.getBean("bean1")).andReturn(new Bean1());
		expect(context.getBean("bean2")).andReturn(new Bean2());
		mapping.setAllowedType(TransactionalUrlHandlerMapping.AllowedType.TRANSACTIONAL_ONLY);
		replay(context);
		assertFalse(mapping.isBeanSupported("bean1"));
		assertTrue(mapping.isBeanSupported("bean2"));
		verify(context);
	}

	private static class Bean1 {
	}

	@Transactional
	private static class Bean2 {
	}

	private static class Bean3 extends Bean2 {
	}

	private static class Bean4 {
		@Transactional
		public void asd() {
		}
	}
}
