package wisematches.core.personality.member.profile.impl;

import org.easymock.Capture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.personality.member.account.*;
import wisematches.core.personality.member.profile.*;

import java.util.Date;
import java.util.UUID;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml"
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
		final Capture<PlayerProfile> profileCapture = new Capture<PlayerProfile>();

		final PlayerProfileListener listener = EasyMock.createStrictMock(PlayerProfileListener.class);
		listener.playerProfileChanged(EasyMock.capture(profileCapture));
		EasyMock.replay(listener);

		profileManager.addPlayerProfileListener(listener);

		final Account account = accountManager.createAccount(createMockAccount());
		final PlayerProfile profile = profileManager.getPlayerProfile(account);
		Assert.assertNotNull(profile);
		Assert.assertEquals(account.getId(), profile.getPlayerId());
		Assert.assertNull(profile.getRealName());
		Assert.assertNull(profile.getCountryCode());
		Assert.assertNull(profile.getBirthday());
		Assert.assertNull(profile.getGender());
		Assert.assertNull(profile.getPrimaryLanguage());

		final Date birthday = new Date();

		final PlayerProfileEditor editor = new PlayerProfileEditor(profile);
		editor.setRealName("Mock Real Name");
		editor.setCountryCode("ru");
		editor.setBirthday(birthday);
		editor.setGender(Gender.FEMALE);
		editor.setPrimaryLanguage(Language.RU);
		profileManager.updateProfile(editor.createProfile());

		final PlayerProfile profileUpdated = profileManager.getPlayerProfile(account);
		Assert.assertEquals(account.getId(), profile.getPlayerId());
		Assert.assertEquals("Mock Real Name", profileUpdated.getRealName());
		Assert.assertEquals("ru", profileUpdated.getCountryCode());
		Assert.assertEquals(birthday, profileUpdated.getBirthday());
		Assert.assertEquals(Gender.FEMALE, profileUpdated.getGender());
		Assert.assertEquals(Language.RU, profileUpdated.getPrimaryLanguage());

		accountManager.removeAccount(account);
		Assert.assertNull(profileManager.getPlayerProfile(account));

		EasyMock.verify(listener);
	}

	private Account createMockAccount() {
		final String id = UUID.randomUUID().toString();
		return new AccountEditor(id + "@mock.junit", id, "mock").createAccount();
	}
}
