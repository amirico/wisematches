package wisematches.server.services.dictionary.impl;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.*;
import wisematches.server.services.dictionary.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateDictionarySuggestionManager implements DictionarySuggestionManager {
	private SessionFactory sessionFactory;
	private DictionaryManager dictionaryManager;

	private final Set<DictionarySuggestionListener> listeners = new CopyOnWriteArraySet<>();

	private static final Logger log = LoggerFactory.getLogger("wisematches.dictionary.SuggestionManager");

	public HibernateDictionarySuggestionManager() {
	}

	@Override
	public void addDictionarySuggestionListener(DictionarySuggestionListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeDictionarySuggestionListener(DictionarySuggestionListener listener) {
		listeners.remove(listener);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void rejectRequests(String commentary, Long... ids) {
		final Session session = sessionFactory.getCurrentSession();

		final List<HibernateWordSuggestion> list = selectRequestsById(session, ids);
		for (HibernateWordSuggestion r : list) {
			r.resolveSuggestion(SuggestionState.REJECTED, commentary);
			session.update(r);

			for (DictionarySuggestionListener listener : listeners) {
				listener.changeRequestRejected(r);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void approveRequests(String commentary, Long... ids) {
		final Session session = sessionFactory.getCurrentSession();
		final List<HibernateWordSuggestion> list = selectRequestsById(session, ids);

		final Set<Dictionary> changed = new HashSet<>();
		for (HibernateWordSuggestion r : list) {
			try {
				final Dictionary dictionary = dictionaryManager.getDictionary(r.getLanguage());
				changed.add(dictionary);

				final WordEntry entry = dictionary.getWordEntry(r.getWord());
				final SuggestionType suggestionType = r.getSuggestionType();
				if (suggestionType == SuggestionType.CREATE) {
					dictionary.addWordEntry(r.createWordEntry());
				} else if (suggestionType == SuggestionType.REMOVE) {
					dictionary.removeWordEntry(entry);
				} else if (suggestionType == SuggestionType.UPDATE) {
					dictionary.updateWordEntry(r.createWordEntry());
				} else {
					throw new IllegalArgumentException("Incorrect change type: " + suggestionType);
				}

				r.resolveSuggestion(SuggestionState.APPROVED, commentary);
				session.update(r);

				for (DictionarySuggestionListener listener : listeners) {
					listener.changeRequestApproved(r);
				}
			} catch (Exception ex) {
				log.error("Change request can't be processed: {}", r, ex);
			}
		}

		for (Dictionary dictionary : changed) {
			try {
				dictionary.flush();
			} catch (DictionaryException e) {
				log.error("Dictionary changes can't be stored: " + dictionary.getLanguage(), e);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void updateRequest(Long id, String definition, EnumSet<WordAttribute> attributes) {
		final Session session = sessionFactory.getCurrentSession();

		final HibernateWordSuggestion r = (HibernateWordSuggestion) session.get(HibernateWordSuggestion.class, id);
		if (r != null) {
			r.updateDefinition(definition, attributes);
		}
		session.save(r);

		for (DictionarySuggestionListener listener : listeners) {
			listener.changeRequestUpdated(r);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public WordSuggestion addWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary.contains(word)) {
			throw new IllegalArgumentException("Word already exist");
		}

		final HibernateWordSuggestion r = new HibernateWordSuggestion(word, person.getId(), language, definition, attributes, SuggestionType.CREATE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionarySuggestionListener listener : listeners) {
			listener.changeRequestRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public WordSuggestion updateWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (!dictionary.contains(word)) {
			throw new IllegalArgumentException("Word is unknown");
		}

		final HibernateWordSuggestion r = new HibernateWordSuggestion(word, person.getId(), language, definition, attributes, SuggestionType.UPDATE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionarySuggestionListener listener : listeners) {
			listener.changeRequestRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public WordSuggestion removeWord(Personality person, Language language, String word) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		final WordEntry entry = dictionary.getWordEntry(word);
		if (entry == null) {
			throw new IllegalArgumentException("Word is unknown");
		}

		final HibernateWordSuggestion r = new HibernateWordSuggestion(word, person.getId(), language, entry.getDefinition(), entry.getAttributes(), SuggestionType.REMOVE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionarySuggestionListener listener : listeners) {
			listener.changeRequestRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends SuggestionContext> int getTotalCount(Personality person, Ctx context) {
		Query q = createSearchQuery(true, person, context, null);
		return ((Number) q.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends SuggestionContext> List<WordSuggestion> searchEntities(Personality person, Ctx context, Orders orders, Range range) {
		Query q = createSearchQuery(false, person, context, orders);
		if (range != null) {
			range.apply(q);
		}
		return q.list();
	}

	private Query createSearchQuery(boolean count, Personality person, SuggestionContext context, Orders orders) {
		final Session session = sessionFactory.getCurrentSession();

		final StringBuilder queryString = new StringBuilder();
		if (count) {
			queryString.append("select count(*) ");
		}
		queryString.append("from HibernateWordSuggestion r where 1=1 ");
		final String word = context.getWord();
		final Language language = context.getLanguage();
		final Date resolvedAfter = context.getResolvedAfter();
		final EnumSet<SuggestionType> suggestionTypes = context.getSuggestionTypes();
		final EnumSet<SuggestionState> suggestionStates = context.getSuggestionStates();

		if (person != null) {
			queryString.append(" and r.requester=:pid");
		}
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
		if (resolvedAfter != null) {
			queryString.append(" and r.resolutionDate >= :resolvedAfter");
		}

		if (orders != null) {
			orders.apply(queryString);
		}

		final Query query = session.createQuery(queryString.toString());
		if (person != null) {
			query.setParameter("pid", person.getId());
		}
		if (word != null) {
			query.setString("word", word);
		}
		if (language != null) {
			query.setParameter("language", language);
		}
		if (resolvedAfter != null) {
			query.setParameter("resolvedAfter", resolvedAfter);
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
	private List<HibernateWordSuggestion> selectRequestsById(Session session, Long[] ids) {
		final Query query = session.createQuery("from HibernateWordSuggestion where id in (:ids)");
		query.setParameterList("ids", ids, LongType.INSTANCE);
		return query.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}
}
