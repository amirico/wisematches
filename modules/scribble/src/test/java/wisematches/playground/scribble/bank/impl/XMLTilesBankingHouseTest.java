package wisematches.playground.scribble.bank.impl;

import junit.framework.TestCase;
import org.springframework.core.io.ClassPathResource;
import wisematches.core.Language;
import wisematches.playground.scribble.bank.TilesBank;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class XMLTilesBankingHouseTest extends TestCase {
	private XMLTilesBankingHouse bankingHouse;

	public XMLTilesBankingHouseTest() {
	}

	protected void setUp() throws Exception {
		bankingHouse = new XMLTilesBankingHouse();
		bankingHouse.setBanksPath(new ClassPathResource("/banks/"));
	}

	public void testGetLettersBank() {
		TilesBank tilesBank = bankingHouse.createTilesBank(Language.EN, 2, true);
		assertEquals(98 + 2, tilesBank.getBankCapacity()); //15 letters and 2 - *
		assertTrue(Character.isLowerCase(tilesBank.requestTiles(1)[0].getLetter()));

		tilesBank = bankingHouse.createTilesBank(Language.EN, 2, false);
		assertEquals(98, tilesBank.getBankCapacity()); //15 letters

		tilesBank = bankingHouse.createTilesBank(Language.RU, 4, true);
		assertEquals(132 + 4, tilesBank.getBankCapacity()); //132 letters and 4 - * (1 for each player)
	}
}
