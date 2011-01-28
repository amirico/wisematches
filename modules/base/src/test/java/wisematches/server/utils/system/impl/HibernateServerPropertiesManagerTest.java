package wisematches.server.utils.system.impl;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.server.utils.system.ServerPropertiesManager;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateServerPropertiesManagerTest extends AbstractTransactionalDataSourceSpringContextTests {
	private ServerPropertiesManager serverPropertiesManager;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-server-base-config.xml"};
	}

	public void testProperties() {
		final MockObject value = serverPropertiesManager.getPropertyValue(MockServerPropertyType.class);
		assertNull(value);

		serverPropertiesManager.setPropertyValue(MockServerPropertyType.class, new MockObject("testThisMethod"));
		final MockObject value2 = serverPropertiesManager.getPropertyValue(MockServerPropertyType.class);
		assertNotNull(value2);
		assertEquals("testThisMethod", value2.getValue());

		final int i = jdbcTemplate.queryForInt("select id value from server_properties where name = \"" + MockServerPropertyType.class.getName() + "\"");
		assertTrue(i != 0);

		final int i1 = jdbcTemplate.update("delete from server_properties where id = " + i);
		assertEquals(1, i1);
	}

	public void setServerPropertiesManager(ServerPropertiesManager serverPropertiesManager) {
		this.serverPropertiesManager = serverPropertiesManager;
	}
}

