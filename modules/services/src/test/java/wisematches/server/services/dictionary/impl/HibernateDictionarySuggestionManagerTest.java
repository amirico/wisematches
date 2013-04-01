package wisematches.server.services.dictionary.impl;

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
import wisematches.server.services.dictionary.*;

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
		"classpath:/config/scribble-config.xml",
		"classpath:/config/playground-config.xml"
})
public class HibernateDictionarySuggestionManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private DictionaryManager dictionaryManager;
	private DictionarySuggestionListener changeListener;
	private HibernateDictionarySuggestionManager changeManager;

	private static final Personality PERSON = new DefaultRobot(RobotType.DULL);

	public HibernateDictionarySuggestionManagerTest() {
	}

	@Before
	public void setUp() {
		dictionaryManager = createMock(DictionaryManager.class);

		changeListener = createNiceMock(DictionarySuggestionListener.class);

		changeManager = new HibernateDictionarySuggestionManager();
		changeManager.addDictionarySuggestionListener(changeListener);

		changeManager.setSessionFactory(sessionFactory);
		changeManager.setDictionaryManager(dictionaryManager);
	}

	@Test
	public void testSearch() {
		changeManager.getTotalCount(null, new SuggestionContext(Language.EN, null, null, null));
		changeManager.searchEntities(null, new SuggestionContext(Language.EN, null, null, null), null, null);

		changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null, null));
		changeManager.searchEntities(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null, null), null, null);

		changeManager.getTotalCount(null, new SuggestionContext(null, null, EnumSet.of(SuggestionState.REJECTED), null));
		changeManager.searchEntities(null, new SuggestionContext(null, null, EnumSet.of(SuggestionState.REJECTED), null), null, null);

		changeManager.getTotalCount(null, new SuggestionContext(Language.EN, EnumSet.of(SuggestionType.REMOVE), EnumSet.of(SuggestionState.REJECTED), null));
		changeManager.searchEntities(null, new SuggestionContext(Language.EN, EnumSet.of(SuggestionType.REMOVE), EnumSet.of(SuggestionState.REJECTED), null), null, null);
	}

	@Test
	public void testAddRemoveUpdate() {
		final Capture<WordSuggestion> requestCapture = new Capture<>(CaptureType.ALL);

		changeListener.changeRequestRaised(capture(requestCapture));
		changeListener.changeRequestRaised(capture(requestCapture));
		changeListener.changeRequestRaised(capture(requestCapture));
		replay(changeListener);

		final Dictionary dictionary = EasyMock.createMock(Dictionary.class);
		expect(dictionary.contains("poofgjnwhj")).andReturn(false);
		expect(dictionary.getWordEntry("poofgjnwhj")).andReturn(new WordEntry("poofgjnwhj"));
		expect(dictionary.contains("poofgjnwhj")).andReturn(true);
		replay(dictionary);

		expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary).times(3);
		replay(dictionaryManager);

		int countAdd = changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.CREATE), null, null));
		changeManager.addWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word", EnumSet.of(WordAttribute.MASCULINE));
		assertEquals(countAdd + 1, changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.CREATE), null, null)));

		int countUpdate = changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.UPDATE), null, null));
		changeManager.updateWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word 2", EnumSet.of(WordAttribute.FEMININE));
		assertEquals(countUpdate + 1, changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.UPDATE), null, null)));

		int countDelete = changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null, null));
		changeManager.removeWord(PERSON, Language.EN, "poofgjnwhj");
		assertEquals(countDelete + 1, changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null, null)));

		assertEquals(3, requestCapture.getValues().size());

		final WordSuggestion add = requestCapture.getValues().get(0);
		assertEquals("poofgjnwhj", add.getWord());
		assertEquals(Language.EN, add.getLanguage());
		assertEquals(PERSON.getId().longValue(), add.getRequester());
		assertEquals(EnumSet.of(WordAttribute.MASCULINE), add.getAttributes());
		assertEquals("This is testing word", add.getDefinition());
		assertEquals(SuggestionState.WAITING, add.getSuggestionState());
		assertEquals(SuggestionType.CREATE, add.getSuggestionType());

		final WordSuggestion update = requestCapture.getValues().get(1);
		assertEquals("poofgjnwhj", update.getWord());
		assertEquals(Language.EN, update.getLanguage());
		assertEquals(PERSON.getId().longValue(), update.getRequester());
		assertEquals(EnumSet.of(WordAttribute.FEMININE), update.getAttributes());
		assertEquals("This is testing word 2", update.getDefinition());
		assertEquals(SuggestionState.WAITING, update.getSuggestionState());
		assertEquals(SuggestionType.UPDATE, update.getSuggestionType());

		final WordSuggestion remove = requestCapture.getValues().get(2);
		assertEquals("poofgjnwhj", remove.getWord());
		assertEquals(Language.EN, remove.getLanguage());
		assertEquals(PERSON.getId().longValue(), remove.getRequester());
		assertEquals("", remove.getDefinition());
		assertEquals(EnumSet.noneOf(WordAttribute.class), remove.getAttributes());
		assertEquals(SuggestionState.WAITING, remove.getSuggestionState());
		assertEquals(SuggestionType.REMOVE, remove.getSuggestionType());

		verify(changeListener, dictionaryManager, dictionary);
	}

	@Test
	public void testApproveReject() throws DictionaryException {
		final Capture<WordSuggestion> requestCapture = new Capture<>(CaptureType.ALL);

		changeListener.changeRequestRaised(capture(requestCapture));
		changeListener.changeRequestRaised(capture(requestCapture));
		changeListener.changeRequestRaised(capture(requestCapture));
		changeListener.changeRequestApproved(capture(requestCapture));
		changeListener.changeRequestRejected(capture(requestCapture));
		changeListener.changeRequestRejected(capture(requestCapture));
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

		final WordSuggestion r1 = changeManager.addWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word", EnumSet.of(WordAttribute.MASCULINE));
		changeManager.approveRequests("mock1", r1.getId());

		final WordSuggestion r2 = changeManager.updateWord(PERSON, Language.EN, "poofgjnwhj", "This is testing word 2", EnumSet.of(WordAttribute.FEMININE));
		final WordSuggestion r3 = changeManager.removeWord(PERSON, Language.EN, "poofgjnwhj");
		changeManager.rejectRequests("mock2", r2.getId(), r3.getId());

		verify(changeListener, dictionaryManager, dictionary);
	}
}
