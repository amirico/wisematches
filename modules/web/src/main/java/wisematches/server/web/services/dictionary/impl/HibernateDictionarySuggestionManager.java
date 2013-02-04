package wisematches.server.web.services.dictionary.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.playground.dictionary.WordEntry;
import wisematches.server.web.services.dictionary.*;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateDictionarySuggestionManager implements DictionarySuggestionManager {
	private SessionFactory sessionFactory;
	private DictionaryManager dictionaryManager;

	private final Set<DictionarySuggestionListener> listeners = new CopyOnWriteArraySet<>();

	private static final Log log = LogFactory.getLog("wisematches.server.dict.");

	public HibernateDictionarySuggestionManager() {
	}

	@Override
	public void addDictionaryChangeListener(DictionarySuggestionListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeDictionaryChangeListener(DictionarySuggestionListener listener) {
		listeners.remove(listener);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void rejectRequests(long... ids) {
		final Session session = sessionFactory.getCurrentSession();

		final List<HibernateChangeSuggestion> list = selectRequestsById(session, ids);
		for (HibernateChangeSuggestion r : list) {
			r.setSuggestionState(SuggestionState.REJECTED);
			session.update(r);

			for (DictionarySuggestionListener listener : listeners) {
				listener.changeRequestRejected(r);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void approveRequests(long... ids) {
		final Session session = sessionFactory.getCurrentSession();
		final List<HibernateChangeSuggestion> list = selectRequestsById(session, ids);
		for (HibernateChangeSuggestion r : list) {
			try {
				final Dictionary dictionary = dictionaryManager.getDictionary(r.getLanguage());
				final WordEntry entry = dictionary.getWordEntry(r.getWord());
				final SuggestionType suggestionType = r.getSuggestionType();
				if (suggestionType == SuggestionType.ADD) {
					dictionary.addWordEntry(r.createWordEntry());
				} else if (suggestionType == SuggestionType.REMOVE) {
					dictionary.removeWordEntry(entry);
				} else if (suggestionType == SuggestionType.UPDATE) {
					dictionary.addWordEntry(r.createWordEntry());
				} else {
					throw new IllegalArgumentException("Incorrect change type: " + suggestionType);
				}

				r.setSuggestionState(SuggestionState.APPROVED);
				session.update(r);

				for (DictionarySuggestionListener listener : listeners) {
					listener.changeRequestApproved(r);
				}
			} catch (Exception ex) {
				log.error("Change request can't be processed: " + r, ex);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public ChangeSuggestion addWord(String word, String definition, EnumSet<WordAttribute> attributes, Language language, Personality person) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary.contains(word)) {
			throw new IllegalArgumentException("Word already exist");
		}

		final HibernateChangeSuggestion r = new HibernateChangeSuggestion(word, person.getId(), language, attributes, definition, SuggestionType.ADD);
		sessionFactory.getCurrentSession().save(r);

		for (DictionarySuggestionListener listener : listeners) {
			listener.changeRequestRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public ChangeSuggestion updateWord(String word, String definition, EnumSet<WordAttribute> attributes, Language language, Personality person) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (!dictionary.contains(word)) {
			throw new IllegalArgumentException("Word is unknown");
		}

		final HibernateChangeSuggestion r = new HibernateChangeSuggestion(word, person.getId(), language, attributes, definition, SuggestionType.UPDATE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionarySuggestionListener listener : listeners) {
			listener.changeRequestRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public ChangeSuggestion removeWord(String word, Language language, Personality person) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (!dictionary.contains(word)) {
			throw new IllegalArgumentException("Word is unknown");
		}

		final HibernateChangeSuggestion r = new HibernateChangeSuggestion(word, person.getId(), language, null, null, SuggestionType.REMOVE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionarySuggestionListener listener : listeners) {
			listener.changeRequestRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends SuggestionContext> int getTotalCount(Personality person, Ctx context) {
		Query q = createSearchQuery(true, context, null);
		return ((Number) q.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends SuggestionContext> List<ChangeSuggestion> searchEntities(Personality person, Ctx context, Orders orders, Range range) {
		Query q = createSearchQuery(false, context, null);
		if (range != null) {
			range.apply(q);
		}
		return q.list();
	}

	private Query createSearchQuery(boolean count, SuggestionContext context, Orders orders) {
		final Session session = sessionFactory.getCurrentSession();

		final StringBuilder queryString = new StringBuilder();
		if (count) {
			queryString.append("select count(*) ");
		}
		queryString.append("from HibernateChangeSuggestion r where 1=1 ");
		final String word = context.getWord();
		final Language language = context.getLanguage();
		final EnumSet<SuggestionType> suggestionTypes = context.getSuggestionTypes();
		final EnumSet<SuggestionState> suggestionStates = context.getSuggestionStates();

		if (word != null) {
			queryString.append(" and r.word=:word");
		}
		if (language != null) {
			queryString.append(" and r.language=:language");
		}
		if (suggestionTypes != null) {
			queryString.append(" and r.suggestionType in (:suggestionTypes)");
		}
		if (suggestionStates != null) {
			queryString.append(" and r.suggestionState in (:suggestionStates)");
		}

		if (orders != null) {
			orders.apply(queryString);
		}

		final Query query = session.createQuery(queryString.toString());
		if (word != null) {
			query.setString("word", word);
		}
		if (language != null) {
			query.setParameter("language", language);
		}
		if (suggestionTypes != null) {
			query.setParameterList("suggestionTypes", suggestionTypes);
		}
		if (suggestionStates != null) {
			query.setParameterList("suggestionStates", suggestionStates);
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	private List<HibernateChangeSuggestion> selectRequestsById(Session session, long[] ids) {
		Long[] l = new Long[ids.length];
		for (int i = 0; i < ids.length; i++) {
			l[i] = ids[i];
		}
		final Query query = session.createQuery("from HibernateChangeSuggestion where id in (:ids)");
		query.setParameterList("ids", l, LongType.INSTANCE);
		return query.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}
}
