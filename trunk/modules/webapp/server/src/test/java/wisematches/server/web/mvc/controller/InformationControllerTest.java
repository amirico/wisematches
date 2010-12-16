/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.mvc.controller;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Locale;

import static junit.framework.Assert.*;

/**
 * @author klimese
 */
public class InformationControllerTest {
	@Test
	public void testparseInfoHolder() throws Exception {
		final Resource r = new ClassPathResource("/i18n/info/parsingTest.properties");

		final InformationController c = new InformationController();
		final InformationHolder holder = c.parseInfoHolder(r);
		assertNotNull(holder);

		assertHolder(holder, "L", "I", "D", 4);
		assertHolder(holder.getItems().get(0), "S1L", "S1I", "S1D", 3);
		assertHolder(holder.getItems().get(0).getItems().get(0), "S1I1L", "S1I1I", "S1I1D", 0);
		assertHolder(holder.getItems().get(0).getItems().get(1), "S1I2L", null, "S1I2D", 0);
		assertHolder(holder.getItems().get(0).getItems().get(2), "S1I3L", null, "S1I3D", 0);

		assertHolder(holder.getItems().get(1), "S2L", null, "S2D", 3);
		assertHolder(holder.getItems().get(1).getItems().get(0), null, "S2I1I", "S2I1D", 0);
		assertHolder(holder.getItems().get(1).getItems().get(1), null, null, "S2I2D", 0);
		assertHolder(holder.getItems().get(1).getItems().get(2), null, null, "S2I3D", 0);

		assertHolder(holder.getItems().get(2), "S12L", null, null, 3);
		assertHolder(holder.getItems().get(2).getItems().get(0), null, "S12I1I", "S12I1D", 0);
		assertHolder(holder.getItems().get(2).getItems().get(1), null, null, "S12I12D", 0);
		assertHolder(holder.getItems().get(2).getItems().get(2), null, null, "S12I13D", 0);


		assertHolder(holder.getItems().get(3), "S123L", null, null, 3);
		assertHolder(holder.getItems().get(3).getItems().get(0), null, "S123I1I", "S123I1D", 0);
		assertHolder(holder.getItems().get(3).getItems().get(1), null, null, "S12I121D", 0);
		assertHolder(holder.getItems().get(3).getItems().get(2), null, null, "S12I132D", 0);
	}

	@Test
	public void testgetInfoHolder() throws Exception {
		final InformationController c = new InformationController();
		assertHolder(c.loadInfoHolder("loadTest", new Locale("en")), "L_default", "I_default", "D_default", 0);
		assertHolder(c.loadInfoHolder("loadTest", new Locale("jp")), "L_default", "I_default", "D_default", 0);
		assertHolder(c.loadInfoHolder("loadTest", new Locale("ru")), "L_ru", "I_ru", "D_ru", 0);
		assertHolder(c.loadInfoHolder("loadTest", new Locale("de")), "L_de", "I_de", "D_de", 0);
	}

	private static void assertHolder(InformationHolder holder,
									 String label, String image, String desc, int items) {
		assertEquals(label, holder.getLabel());
		assertEquals(image, holder.getImage());
		assertEquals(desc, holder.getDescription());
		if (items == 0) {
			assertNull(holder.getItems());
		} else {
			assertEquals(items, holder.getItems().size());
		}
	}
}
