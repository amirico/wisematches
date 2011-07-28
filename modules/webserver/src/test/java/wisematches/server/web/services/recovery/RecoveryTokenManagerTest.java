package wisematches.server.web.services.recovery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.account.*;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-junit-config.xml",
		"classpath:/config/application-settings.xml",
		"classpath:/config/server-web-junit-config.xml"
})
public class RecoveryTokenManagerTest {
	@Autowired
	private AccountManager accountManager;

	@Autowired
	private RecoveryTokenManager recoveryTokenManager;

	public RecoveryTokenManagerTest() {
	}

	@Test
	public void testManager() throws InterruptedException, TokenExpiredException, InadmissibleUsernameException, DuplicateAccountException, UnknownAccountException {
		final AccountEditor editor = new AccountEditor();
		editor.setNickname("mock");
		editor.setEmail("mock@wm.net");
		editor.setPassword("mock");

		final Account p = accountManager.createAccount(editor.createAccount());

		final RecoveryToken token = recoveryTokenManager.createToken(p);
		assertNotNull(token);
		assertSame(p, token.getPlayer());
		assertNotNull(token.getToken());
		assertNotNull(token.getDate());

		Thread.sleep(100);
		assertNotSame(token, recoveryTokenManager.createToken(p));

		final RecoveryToken token1 = recoveryTokenManager.getToken(p);
		assertEquals(token.getPlayer(), token1.getPlayer());
		assertFalse(token.getToken().equals(token1.getToken()));
		assertFalse(token.getDate().equals(token1.getDate()));

		recoveryTokenManager.setTokenExpirationTime(300);
		Thread.sleep(400);
		try {
			recoveryTokenManager.getToken(p);
			fail("Exception must be here");
		} catch (TokenExpiredException ex) {
			;
		}

		final RecoveryToken token2 = recoveryTokenManager.createToken(p);
		assertNotNull(token2);

		recoveryTokenManager.removeToken(token2);
		assertNull(recoveryTokenManager.getToken(p));

		accountManager.removeAccount(p);
	}
}
