package wisematches.playground.scribble.settings.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.playground.scribble.MockPlayer;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-junit-config.xml"
})
public class HibernateBoardSettingsManagerTest {
	@Autowired
	private BoardSettingsManager boardSettingsManager;

	public HibernateBoardSettingsManagerTest() {
	}

	@Test
	public void test() {
		final BoardSettings s1 = boardSettingsManager.getScribbleSettings(new MockPlayer(123));
		assertSettings(s1, true, true, true, "tiles-set-classic");

		final BoardSettings s2 = boardSettingsManager.getScribbleSettings(new MockPlayer(1001));
		assertSettings(s2, true, true, true, "tiles-set-classic");
		s2.setTilesClass("mock-tiles");
		s2.setCheckWords(false);
		s2.setCleanMemory(false);
		s2.setShowCaptions(false);
		boardSettingsManager.setScribbleSettings(new MockPlayer(1001), s2);

		final BoardSettings s3 = boardSettingsManager.getScribbleSettings(new MockPlayer(1001));
		assertSettings(s3, false, false, false, "mock-tiles");
		s3.setTilesClass("tiles-set-classic");
		s3.setCheckWords(true);
		s3.setCleanMemory(true);
		s3.setShowCaptions(true);
		boardSettingsManager.setScribbleSettings(new MockPlayer(1001), s3);
	}

	private void assertSettings(BoardSettings s, boolean checkWords, boolean clearMemory, boolean showCaptions, String tilesClass) {
		assertEquals(s.getTilesClass(), tilesClass);
		assertEquals(s.isCheckWords(), checkWords);
		assertEquals(s.isCleanMemory(), clearMemory);
		assertEquals(s.isShowCaptions(), showCaptions);
	}
}
