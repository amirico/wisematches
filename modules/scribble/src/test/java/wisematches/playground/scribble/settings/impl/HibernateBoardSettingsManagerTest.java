package wisematches.playground.scribble.settings.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/conf/configuration.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-config.xml"
})
public class HibernateBoardSettingsManagerTest {
	@Autowired
	private BoardSettingsManager boardSettingsManager;

	public HibernateBoardSettingsManagerTest() {
	}

	@Test
	public void test() {
		final Player p1 = new DefaultMember(900, null, null, null, null, null);
		final Player p2 = new DefaultMember(901, null, null, null, null, null);

		final BoardSettings s1 = boardSettingsManager.getScribbleSettings(p2);
		assertSettings(s1, true, true, true, "tiles-set-classic");

		final BoardSettings s2 = boardSettingsManager.getScribbleSettings(p1);
		assertSettings(s2, true, true, true, "tiles-set-classic");
		s2.setTilesClass("mock-tiles");
		s2.setCheckWords(false);
		s2.setCleanMemory(false);
		s2.setShowCaptions(false);
		boardSettingsManager.setScribbleSettings(p1, s2);

		final BoardSettings s3 = boardSettingsManager.getScribbleSettings(p1);
		assertSettings(s3, false, false, false, "mock-tiles");
		s3.setTilesClass("tiles-set-classic");
		s3.setCheckWords(true);
		s3.setCleanMemory(true);
		s3.setShowCaptions(true);
		boardSettingsManager.setScribbleSettings(p1, s3);
	}

	private void assertSettings(BoardSettings s, boolean checkWords, boolean clearMemory, boolean showCaptions, String tilesClass) {
		assertEquals(s.getTilesClass(), tilesClass);
		assertEquals(s.isCheckWords(), checkWords);
		assertEquals(s.isCleanMemory(), clearMemory);
		assertEquals(s.isShowCaptions(), showCaptions);
	}
}
