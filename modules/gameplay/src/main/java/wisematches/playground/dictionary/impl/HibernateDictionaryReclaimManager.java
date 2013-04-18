package wisematches.playground.dictionary.impl;


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

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateDictionaryReclaimManager implements DictionaryReclaimManager {
	private SessionFactory sessionFactory;
	private DictionaryManager dictionaryManager;

	private final Set<DictionaryReclaimListener> listeners = new CopyOnWriteArraySet<>();

	private static final Logger log = LoggerFactory.getLogger("wisematches.dictionary.ReclaimManager");

	public HibernateDictionaryReclaimManager() {
	}

	@Override
	public void addDictionaryReclaimListener(DictionaryReclaimListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeDictionaryReclaimListener(DictionaryReclaimListener listener) {
		listeners.remove(listener);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void rejectReclaims(String commentary, Long... ids) {
		final Session session = sessionFactory.getCurrentSession();

		final List<HibernateWordReclaim> list = selectRequestsById(session, ids);
		for (HibernateWordReclaim r : list) {
			r.resolveReclaim(ReclaimResolution.REJECTED, commentary);
			session.update(r);

			for (DictionaryReclaimListener listener : listeners) {
				listener.wordReclaimResolved(r, ReclaimResolution.REJECTED);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void approveReclaims(String commentary, Long... ids) {
		final Session session = sessionFactory.getCurrentSession();
		final List<HibernateWordReclaim> list = selectRequestsById(session, ids);

		final Set<Dictionary> changed = new HashSet<>();
		for (HibernateWordReclaim r : list) {
			try {
				final Dictionary dictionary = dictionaryManager.getDictionary(r.getLanguage());
				changed.add(dictionary);

				final WordEntry entry = dictionary.getWordEntry(r.getWord());
				final ReclaimType resolutionType = r.getResolutionType();
				if (resolutionType == ReclaimType.CREATE) {
					dictionary.addWordEntry(r.createWordEntry());
				} else if (resolutionType == ReclaimType.REMOVE) {
					dictionary.removeWordEntry(entry);
				} else if (resolutionType == ReclaimType.UPDATE) {
					dictionary.updateWordEntry(r.createWordEntry());
				} else {
					throw new IllegalArgumentException("Incorrect change type: " + resolutionType);
				}

				r.resolveReclaim(ReclaimResolution.APPROVED, commentary);
				session.update(r);

				for (DictionaryReclaimListener listener : listeners) {
					listener.wordReclaimResolved(r, ReclaimResolution.APPROVED);
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
	public void updateReclaim(Long id, String definition, EnumSet<WordAttribute> attributes) {
		final Session session = sessionFactory.getCurrentSession();

		final HibernateWordReclaim r = (HibernateWordReclaim) session.get(HibernateWordReclaim.class, id);
		if (r != null) {
			r.updateDefinition(definition, attributes);
		}
		session.save(r);

		for (DictionaryReclaimListener listener : listeners) {
			listener.wordReclaimUpdated(r);
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public WordReclaim addWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary.contains(word)) {
			throw new IllegalArgumentException("Word already exist");
		}

		final HibernateWordReclaim r = new HibernateWordReclaim(word, person.getId(), language, definition, attributes, ReclaimType.CREATE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionaryReclaimListener listener : listeners) {
			listener.wordReclaimRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public WordReclaim updateWord(Personality person, Language language, String word, String definition, EnumSet<WordAttribute> attributes) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (!dictionary.contains(word)) {
			throw new IllegalArgumentException("Word is unknown");
		}

		final HibernateWordReclaim r = new HibernateWordReclaim(word, person.getId(), language, definition, attributes, ReclaimType.UPDATE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionaryReclaimListener listener : listeners) {
			listener.wordReclaimRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public WordReclaim removeWord(Personality person, Language language, String word) {
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		final WordEntry entry = dictionary.getWordEntry(word);
		if (entry == null) {
			throw new IllegalArgumentException("Word is unknown");
		}

		final HibernateWordReclaim r = new HibernateWordReclaim(word, person.getId(), language, entry.getDefinition(), entry.getAttributes(), ReclaimType.REMOVE);
		sessionFactory.getCurrentSession().save(r);

		for (DictionaryReclaimListener listener : listeners) {
			listener.wordReclaimRaised(r);
		}
		return r;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends WordReclaimContext> int getTotalCount(Personality person, Ctx context) {
		Query q = createSearchQuery(true, person, context, null);
		return ((Number) q.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends WordReclaimContext> List<WordReclaim> searchEntities(Personality person, Ctx context, Orders orders, Range range) {
		Query q = createSearchQuery(false, person, context, orders);
		if (range != null) {
			range.apply(q);
		}
		return q.list();
	}

	private Query createSearchQuery(boolean count, Personality person, WordReclaimContext context, Orders orders) {
		final Session session = sessionFactory.getCurrentSession();

		final StringBuilder queryString = new StringBuilder();
		if (count) {
			queryString.append("select count(*) ");
		}
		queryString.append("from HibernateWordReclaim r where 1=1 ");
		final String word = context.getWord();
		final Language language = context.getLanguage();
		final Date resolvedAfter = context.getResolvedAfter();
		final Set<ReclaimType> types = context.getReclaimTypes();
		final Set<ReclaimResolution> resolutions = context.getReclaimResolutions();

		if (person != null) {
			queryString.append(" and r.requester=:pid");
		}
		if (word != null) {
			queryString.append(" and r.word=:word");
		}
		if (language != null) {
			queryString.append(" and r.language=:language");
		}
		if (types != null) {
			queryString.append(" and r.resolutionType in (:types)");
		}
		if (resolutions != null) {
			queryString.append(" and r.resolution in (:resolutions)");
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
		if (resolutions != null) {
			query.setParameterList("resolutions", resolutions);
		}
		if (resolvedAfter != null) {
			query.setParameter("resolvedAfter", resolvedAfter);
		}
		if (types != null) {
			query.setParameterList("types", types);
		}
		return query;
	}

	@SuppressWarnings("unchecked")
	private List<HibernateWordReclaim> selectRequestsById(Session session, Long[] ids) {
		final Query query = session.createQuery("from HibernateWordReclaim where id in (:ids)");
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
