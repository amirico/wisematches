package wisematches.core.personality.player.profile.impl;

import org.easymock.Capture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.personality.player.account.*;
import wisematches.core.personality.player.profile.*;

import java.util.Date;
import java.util.UUID;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-junit-config.xml"
})
public class HibernatePlayerProfileManagerTest {
	@Autowired
	private AccountManager accountManager;

	@Autowired
	private PlayerProfileManager profileManager;

	public HibernatePlayerProfileManagerTest() {
	}

	@Test
	public void test() throws InadmissibleUsernameException, DuplicateAccountException, UnknownAccountException {
		final Capture<PlayerProfile> profileCapture = new Capture<>();

		final Account account = accountManager.createAccount(createMockAccount(), "mock");
		final PlayerProfile profile = profileManager.getPlayerProfile(account);
		assertNotNull(profile);
		assertNull(profile.getRealName());
		assertNull(profile.getCountryCode());
		assertNull(profile.getBirthday());
		assertNull(profile.getGender());
		assertNull(profile.getPrimaryLanguage());


		final PlayerProfileListener listener = createStrictMock(PlayerProfileListener.class);
		listener.playerProfileChanged(same(account), capture(profileCapture));
		replay(listener);

		profileManager.addPlayerProfileListener(listener);

		final Date birthday = new Date();

		final PlayerProfileEditor editor = new PlayerProfileEditor(profile);
		editor.setRealName("Mock Real Name");
		editor.setCountryCode("ru");
		editor.setBirthday(birthday);
		editor.setGender(Gender.FEMALE);
		editor.setPrimaryLanguage(Language.RU);
		profileManager.updateProfile(account, editor.createProfile());

		final PlayerProfile profileUpdated = profileManager.getPlayerProfile(account);
		assertEquals("Mock Real Name", profileUpdated.getRealName());
		assertEquals("ru", profileUpdated.getCountryCode());
		assertEquals(birthday, profileUpdated.getBirthday());
		assertEquals(Gender.FEMALE, profileUpdated.getGender());
		assertEquals(Language.RU, profileUpdated.getPrimaryLanguage());

		accountManager.removeAccount(account);
		assertNull(profileManager.getPlayerProfile(account));

		verify(listener);
	}

	private Account createMockAccount() {
		final String id = UUID.randomUUID().toString();
		return new AccountEditor(id + "@mock.junit", id).createAccount();
	}
}
