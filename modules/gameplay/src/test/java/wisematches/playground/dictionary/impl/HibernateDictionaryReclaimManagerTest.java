package wisematches.playground.dictionary.impl;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.RobotType;
import wisematches.core.personality.DefaultRobot;
import wisematches.playground.dictionary.*;

import java.util.EnumSet;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/gameplay-config.xml"
})
public class HibernateDictionaryReclaimManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private DictionaryManager dictionaryManager;
	private DictionaryReclaimListener changeListener;
	private HibernateDictionaryReclaimManager changeManager;

	private static final Personality PERSON = new DefaultRobot(RobotType.DULL);

	public HibernateDictionaryReclaimManagerTest() {
	}

	@Before
	public void setUp() {
		dictionaryManager = createMock(DictionaryManager.class);

		changeListener = createNiceMock(DictionaryReclaimListener.class);

		changeManager = new HibernateDictionaryReclaimManager();
		changeManager.addDictionaryReclaimListener(changeListener);

		changeManager.setSessionFactory(sessionFactory);
		changeManager.setDictionaryManager(dictionaryManager);
	}

	@Test
	public void testSearch() {
		changeManager.getTotalCount(null, new WordReclaimContext(Language.EN, null, null, null));
		changeManager.searchEntities(null, new WordReclaimContext(Language.EN, null, null, null), null, null);

		changeManager.getTotalCount(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.REMOVE), null, null));
		changeManager.searchEntities(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.REMOVE), null, null), null, null);

		changeManager.getTotalCount(null, new WordReclaimContext(null, null, EnumSet.of(ReclaimResolution.REJECTED), null));
		changeManager.searchEntities(null, new WordReclaimContext(null, null, EnumSet.of(ReclaimResolution.REJECTED), null), null, null);

		changeManager.getTotalCount(null, new WordReclaimContext(Language.EN, EnumSet.of(ReclaimType.REMOVE), EnumSet.of(ReclaimResolution.REJECTED), null));
		changeManager.searchEntities(null, new WordReclaimContext(Language.EN, EnumSet.of(ReclaimType.REMOVE), EnumSet.of(ReclaimResolution.REJECTED), null), null, null);
	}

	@Test
	public void testAddRemoveUpdate() {
		final Capture<WordReclaim> requestCapture = new Capture<>(CaptureType.ALL);

		changeListener.wordReclaimRaised(capture(requestCapture));
		changeListener.wordReclaimRaised(capture(requestCapture));
		changeListener.wordReclaimRaised(capture(requestCapture));
		replay(changeListener);

		final Dictionary dictionary = EasyMock.createMock(Dictionary.class);
		expect(dictionary.contains("poofgjnwhj")).andReturn(false);
		expect(dictionary.getWordEntry("poofgjnwhj")).andReturn(new WordEntry("poofgjnwhj"));
		expect(dictionary.contains("poofgjnwhj")).andReturn(true);
		replay(dictionary);

		expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary).times(3);
		replay(dictionaryManager);

		int countAdd = changeManager.getTotalCount(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.CREATE), null, null));
		changeManager.addWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word", EnumSet.of(WordAttribute.MASCULINE));
		assertEquals(countAdd + 1, changeManager.getTotalCount(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.CREATE), null, null)));

		int countUpdate = changeManager.getTotalCount(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.UPDATE), null, null));
		changeManager.updateWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word 2", EnumSet.of(WordAttribute.FEMININE));
		assertEquals(countUpdate + 1, changeManager.getTotalCount(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.UPDATE), null, null)));

		int countDelete = changeManager.getTotalCount(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.REMOVE), null, null));
		changeManager.removeWord(PERSON, Language.EN, "poofgjnwhj");
		assertEquals(countDelete + 1, changeManager.getTotalCount(null, new WordReclaimContext(null, EnumSet.of(ReclaimType.REMOVE), null, null)));

		assertEquals(3, requestCapture.getValues().size());

		final WordReclaim add = requestCapture.getValues().get(0);
		assertEquals("poofgjnwhj", add.getWord());
		assertEquals(Language.EN, add.getLanguage());
		assertEquals(PERSON.getId().longValue(), add.getRequester());
		assertEquals(EnumSet.of(WordAttribute.MASCULINE), add.getAttributes());
		assertEquals("This is testing word", add.getDefinition());
		assertEquals(ReclaimResolution.WAITING, add.getResolution());
		assertEquals(ReclaimType.CREATE, add.getResolutionType());

		final WordReclaim update = requestCapture.getValues().get(1);
		assertEquals("poofgjnwhj", update.getWord());
		assertEquals(Language.EN, update.getLanguage());
		assertEquals(PERSON.getId().longValue(), update.getRequester());
		assertEquals(EnumSet.of(WordAttribute.FEMININE), update.getAttributes());
		assertEquals("This is testing word 2", update.getDefinition());
		assertEquals(ReclaimResolution.WAITING, update.getResolution());
		assertEquals(ReclaimType.UPDATE, update.getResolutionType());

		final WordReclaim remove = requestCapture.getValues().get(2);
		assertEquals("poofgjnwhj", remove.getWord());
		assertEquals(Language.EN, remove.getLanguage());
		assertEquals(PERSON.getId().longValue(), remove.getRequester());
		assertEquals("", remove.getDefinition());
		assertEquals(EnumSet.noneOf(WordAttribute.class), remove.getAttributes());
		assertEquals(ReclaimResolution.WAITING, remove.getResolution());
		assertEquals(ReclaimType.REMOVE, remove.getResolutionType());

		verify(changeListener, dictionaryManager, dictionary);
	}

	@Test
	public void testApproveReject() throws DictionaryException {
		final Capture<WordReclaim> requestCapture = new Capture<>(CaptureType.ALL);

		changeListener.wordReclaimRaised(capture(requestCapture));
		changeListener.wordReclaimRaised(capture(requestCapture));
		changeListener.wordReclaimRaised(capture(requestCapture));
		changeListener.wordReclaimResolved(capture(requestCapture), same(ReclaimResolution.APPROVED));
		changeListener.wordReclaimResolved(capture(requestCapture), same(ReclaimResolution.REJECTED));
		changeListener.wordReclaimResolved(capture(requestCapture), same(ReclaimResolution.REJECTED));
		replay(changeListener);

		final Dictionary dictionary = EasyMock.createMock(Dictionary.class);
		expect(dictionary.contains("poofgjnwhj")).andReturn(false);
		expect(dictionary.getWordEntry("poofgjnwhj")).andReturn(new WordEntry("poofgjnwhj"));
		dictionary.addWordEntry(isA(WordEntry.class));

		expect(dictionary.contains("poofgjnwhj")).andReturn(true);
		expect(dictionary.getWordEntry("poofgjnwhj")).andReturn(new WordEntry("poofgjnwhj"));
		dictionary.flush();
		replay(dictionary);

		expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary).anyTimes();
		replay(dictionaryManager);

		final WordReclaim r1 = changeManager.addWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word", EnumSet.of(WordAttribute.MASCULINE));
		changeManager.approveReclaims("mock1", r1.getId());

		final WordReclaim r2 = changeManager.updateWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word 2", EnumSet.of(WordAttribute.FEMININE));
		final WordReclaim r3 = changeManager.removeWord(PERSON, Language.EN, "poofgjnwhj");
		changeManager.rejectReclaims("mock2", r2.getId(), r3.getId());

		verify(changeListener, dictionaryManager, dictionary);
	}
}
