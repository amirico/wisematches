package wisematches.server.web.services.dictionary.impl;

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
import wisematches.playground.dictionary.*;
import wisematches.server.web.services.dictionary.*;

import java.util.EnumSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
})
public class HibernateDictionarySuggestionManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private DictionaryManager dictionaryManager;
	private DictionarySuggestionListener changeListener;
	private HibernateDictionarySuggestionManager changeManager;

	public HibernateDictionarySuggestionManagerTest() {
	}

	@Before
	public void setUp() {
		dictionaryManager = createMock(DictionaryManager.class);

		changeListener = createNiceMock(DictionarySuggestionListener.class);

		changeManager = new HibernateDictionarySuggestionManager();
		changeManager.addDictionaryChangeListener(changeListener);

		changeManager.setSessionFactory(sessionFactory);
		changeManager.setDictionaryManager(dictionaryManager);
	}

	@Test
	public void testSearch() {
		changeManager.getTotalCount(null, new SuggestionContext(Language.EN, null, null));
		changeManager.searchEntities(null, new SuggestionContext(Language.EN, null, null), null, null, null);

		changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null));
		changeManager.searchEntities(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null), null, null, null);

		changeManager.getTotalCount(null, new SuggestionContext(null, null, EnumSet.of(SuggestionState.REJECTED)));
		changeManager.searchEntities(null, new SuggestionContext(null, null, EnumSet.of(SuggestionState.REJECTED)), null, null, null);

		changeManager.getTotalCount(null, new SuggestionContext(Language.EN, EnumSet.of(SuggestionType.REMOVE), EnumSet.of(SuggestionState.REJECTED)));
		changeManager.searchEntities(null, new SuggestionContext(Language.EN, EnumSet.of(SuggestionType.REMOVE), EnumSet.of(SuggestionState.REJECTED)), null, null, null);
	}

	@Test
	public void testAddRemoveUpdate() {
		final Capture<ChangeSuggestion> requestCapture = new Capture<>(CaptureType.ALL);

		changeListener.changeRequestRaised(capture(requestCapture));
		changeListener.changeRequestRaised(capture(requestCapture));
		changeListener.changeRequestRaised(capture(requestCapture));
		replay(changeListener);

		final Dictionary dictionary = EasyMock.createMock(Dictionary.class);
		expect(dictionary.contains("poofgjnwhj")).andReturn(false);
		expect(dictionary.contains("poofgjnwhj")).andReturn(true);
		expect(dictionary.contains("poofgjnwhj")).andReturn(true);
		replay(dictionary);

		expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary).times(3);
		replay(dictionaryManager);

		int countAdd = changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.ADD), null));
		changeManager.addWord("poofgjnwhj", "This is testing word", EnumSet.of(WordAttribute.MASCULINE), Language.EN, Personality.person(101));
		assertEquals(countAdd + 1, changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.ADD), null)));

		int countUpdate = changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.UPDATE), null));
		changeManager.updateWord("poofgjnwhj", "This is testing word 2", EnumSet.of(WordAttribute.FEMININE), Language.EN, Personality.person(101));
		assertEquals(countUpdate + 1, changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.UPDATE), null)));

		int countDelete = changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null));
		changeManager.removeWord("poofgjnwhj", Language.EN, Personality.person(101));
		assertEquals(countDelete + 1, changeManager.getTotalCount(null, new SuggestionContext(null, EnumSet.of(SuggestionType.REMOVE), null)));

		assertEquals(3, requestCapture.getValues().size());

		final ChangeSuggestion add = requestCapture.getValues().get(0);
		assertEquals("poofgjnwhj", add.getWord());
		assertEquals(Language.EN, add.getLanguage());
		assertEquals(101L, add.getRequester());
		assertEquals(EnumSet.of(WordAttribute.MASCULINE), add.getAttributes());
		assertEquals("This is testing word", add.getDefinition());
		assertEquals(SuggestionState.WAITING, add.getSuggestionState());
		assertEquals(SuggestionType.ADD, add.getSuggestionType());

		final ChangeSuggestion update = requestCapture.getValues().get(1);
		assertEquals("poofgjnwhj", update.getWord());
		assertEquals(Language.EN, update.getLanguage());
		assertEquals(101L, update.getRequester());
		assertEquals(EnumSet.of(WordAttribute.FEMININE), update.getAttributes());
		assertEquals("This is testing word 2", update.getDefinition());
		assertEquals(SuggestionState.WAITING, update.getSuggestionState());
		assertEquals(SuggestionType.UPDATE, update.getSuggestionType());

		final ChangeSuggestion remove = requestCapture.getValues().get(2);
		assertEquals("poofgjnwhj", remove.getWord());
		assertEquals(Language.EN, remove.getLanguage());
		assertEquals(101L, remove.getRequester());
		assertNull(remove.getAttributes());
		assertNull(remove.getDefinition());
		assertEquals(SuggestionState.WAITING, remove.getSuggestionState());
		assertEquals(SuggestionType.REMOVE, remove.getSuggestionType());

		verify(changeListener, dictionaryManager, dictionary);
	}

	@Test
	public void testApproveReject() throws DictionaryException {
		final Capture<ChangeSuggestion> requestCapture = new Capture<>(CaptureType.ALL);

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
		expect(dictionary.contains("poofgjnwhj")).andReturn(true);
		replay(dictionary);

		expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary).anyTimes();
		replay(dictionaryManager);

		final ChangeSuggestion r1 = changeManager.addWord("poofgjnwhj", "This is testing word", EnumSet.of(WordAttribute.MASCULINE), Language.EN, Personality.person(101));
		changeManager.approveRequests(r1.getId());

		final ChangeSuggestion r2 = changeManager.updateWord("poofgjnwhj", "This is testing word 2", EnumSet.of(WordAttribute.FEMININE), Language.EN, Personality.person(101));
		final ChangeSuggestion r3 = changeManager.removeWord("poofgjnwhj", Language.EN, Personality.person(101));
		changeManager.rejectRequests(r2.getId(), r3.getId());

		verify(changeListener, dictionaryManager, dictionary);
	}
}