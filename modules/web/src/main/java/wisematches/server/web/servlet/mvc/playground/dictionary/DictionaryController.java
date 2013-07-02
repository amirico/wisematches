package wisematches.server.web.servlet.mvc.playground.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.search.Order;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.*;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.dictionary.form.WordDefinitionForm;
import wisematches.server.web.servlet.mvc.playground.dictionary.form.WordResolutionForm;
import wisematches.server.web.servlet.sdo.DataTablesRequest;
import wisematches.server.web.servlet.sdo.ServiceResponse;
import wisematches.server.web.servlet.sdo.dictionary.WordReclaimInfo;

import java.util.*;

import static java.util.EnumSet.of;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/dictionary")
public class DictionaryController extends WisematchesController {
	private DictionaryManager dictionaryManager;
	private DictionaryReclaimManager dictionaryReclaimManager;

	private static final EnumSet<ReclaimType> RECLAIM_REMOVE = of(ReclaimType.REMOVE);
	private static final EnumSet<ReclaimType> RECLAIM_CREATE = of(ReclaimType.CREATE);

	private static final EnumSet<ReclaimResolution> RESOLUTIONS_WAITING = of(ReclaimResolution.WAITING);
	private static final EnumSet<ReclaimResolution> RESOLUTIONS_APPROVED = of(ReclaimResolution.APPROVED);

	private static final Orders RESOLUTION_ORDERS = Orders.of(Order.desc("resolutionDate"), Order.desc("requestDate"), Order.asc("resolutionType"));

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.DictionaryController");

	public DictionaryController() {
	}

	@RequestMapping("view")
	public String viewPage(@RequestParam(value = "l", required = false) String lang, Model model, Locale locale) throws UnknownEntityException {
		final Language language = getLanguage(lang, locale);
		if (language == null) {
			throw new UnknownEntityException(language, "dictionary");
		}

		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary == null) {
			throw new UnknownEntityException(language, "dictionary");
		}

		model.addAttribute("dictionary", dictionary);
		return "/content/playground/dictionary/view";
	}

	@RequestMapping("personal")
	public String personalPage(@RequestParam(value = "p", required = false) Long pid, Model model) throws UnknownEntityException {
		final Personality person = (pid == null ? getPrincipal() : personalityManager.getPerson(pid));
		model.addAttribute("player", person);
		return "/content/playground/dictionary/personal";
	}

	@RequestMapping("expectant")
	public String expectantPage(@RequestParam(value = "l", required = false) String lang, Model model, Locale locale) throws UnknownEntityException {
		final Language language = lang != null ? getLanguage(lang, locale) : null;
		final WordReclaimContext ctx = new WordReclaimContext(language, null, RESOLUTIONS_WAITING, null);
		model.addAttribute("reclaims", dictionaryReclaimManager.searchEntities(null, ctx, null, null));
		return "/content/playground/dictionary/expectant";
	}

	@RequestMapping("personalWordEntries.ajax")
	public ServiceResponse personalWordEntriesService(@RequestParam(value = "pid") Long pid,
													  @RequestParam(value = "type", required = false) String[] types,
													  @RequestParam(value = "state", required = false) String[] states,
													  @RequestBody DataTablesRequest request, Locale locale) {
		final Personality person = personalityManager.getPerson(pid);
		Set<ReclaimResolution> ss = null;
		if (states != null) {
			ss = new HashSet<>(states.length);
			for (String state : states) {
				ss.add(ReclaimResolution.valueOf(state));
			}
		}

		Set<ReclaimType> tt = null;
		if (types != null) {
			tt = new HashSet<>(types.length);
			for (String state : types) {
				tt.add(ReclaimType.valueOf(state));
			}
		}

		final WordReclaimContext ctx = new WordReclaimContext(null, tt, ss, null);

		final int totalCount = dictionaryReclaimManager.getTotalCount(person, ctx);
		final List<WordReclaim> reclaims = dictionaryReclaimManager.searchEntities(person, ctx, request.getOrders(), request.getLimit());
		final List<WordReclaimInfo> res = new ArrayList<>(reclaims.size());
		for (WordReclaim s : reclaims) {
			final Personality p = personalityManager.getPerson(s.getRequester());
			res.add(new WordReclaimInfo(p, s, playerStateManager, messageSource, locale));
		}
		return responseFactory.success(request.replay(totalCount, res));
	}

	@RequestMapping("loadWordEntry.ajax")
	public ServiceResponse loadWordEntryService(@RequestParam("l") String lang, @RequestParam("w") String word, Locale locale) {
		final Language language;
		try {
			language = Language.valueOf(lang.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure("dict.suggest.err.language", locale);
		}
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary == null) {
			return responseFactory.failure("dict.suggest.err.dictionary", locale);
		}
		return responseFactory.success(dictionary.getWordEntry(word.toLowerCase()));
	}

	@RequestMapping("loadWordEntries.ajax")
	public ServiceResponse loadWordEntriesService(@RequestParam("l") String lang, @RequestParam("p") String prefix, Locale locale) {
		final Language language;
		try {
			language = Language.valueOf(lang.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure("dict.suggest.err.language", locale);
		}
		final Dictionary dictionary = dictionaryManager.getDictionary(language);
		if (dictionary == null) {
			return responseFactory.failure("dict.suggest.err.dictionary", locale);
		}
		return responseFactory.success(dictionary.getWordEntries(prefix.toLowerCase()));
	}

	@RequestMapping("loadRecentReclaims.ajax")
	public ServiceResponse loadRecentReclaimsService(@RequestParam("l") String lang, Locale locale) {
		final Language language;
		try {
			language = Language.valueOf(lang.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure("dict.suggest.err.language", locale);
		}

		final Date dt = new Date(System.currentTimeMillis() - 604800000L); // 7 days
		final List<WordReclaim> reclaims = dictionaryReclaimManager.searchEntities(null, new WordReclaimContext(language, null, RESOLUTIONS_APPROVED, dt), RESOLUTION_ORDERS, null);
		int index = 0;
		final WordReclaimInfo[] res = new WordReclaimInfo[reclaims.size()];
		for (WordReclaim reclaim : reclaims) {
			final Personality person = personalityManager.getPerson(reclaim.getRequester());
			res[index++] = new WordReclaimInfo(person, reclaim, playerStateManager, messageSource, locale);
		}
		return responseFactory.success(res);
	}

	@Secured("moderator")
	@RequestMapping("update.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse updateService(@RequestBody WordDefinitionForm form, Locale locale) {
		try {
			final EnumSet<WordAttribute> wordAttributes = getWordAttributes(form.getAttributes());
			dictionaryReclaimManager.updateReclaim(form.getId(), form.getDefinition(), wordAttributes);
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure(ex.getMessage(), locale);
		} catch (Exception ex) {
			log.error("Approval request can't be processed: {}", form, ex);
			return responseFactory.failure("dict.suggest.err.system", locale);
		}
		return responseFactory.success();
	}

	@RequestMapping("suggest.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse suggestService(@RequestBody WordDefinitionForm form, Locale locale) {
		Language language;
		try {
			language = Language.valueOf(form.getLanguage().toUpperCase());
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure("dict.suggest.err.language", locale);
		}

		final ReclaimType reclaimType;
		try {
			reclaimType = ReclaimType.valueOf(form.getAction().toUpperCase());
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure("dict.suggest.err.action", locale);
		}

		final String word = form.getWord().toLowerCase();
		if (word.length() < 2) {
			return responseFactory.failure("dict.suggest.err.short", locale);
		}

		final String definition = form.getDefinition();
		if (definition.length() < 2) {
			return responseFactory.failure("dict.suggest.err.desc", locale);
		}

		if (!language.getAlphabet().validate(word)) {
			return responseFactory.failure("dict.suggest.err.alphabet", locale);
		}

		EnumSet<WordAttribute> attributes = null;
		if (reclaimType != ReclaimType.REMOVE) {
			try {
				attributes = getWordAttributes(form.getAttributes());
			} catch (IllegalArgumentException ex) {
				return responseFactory.failure(ex.getMessage(), locale);
			}
		}

		List<WordReclaim> reclaims;
		WordReclaimContext ctx = new WordReclaimContext(word, null, null, RESOLUTIONS_WAITING, null);
		final int cnt = dictionaryReclaimManager.getTotalCount(null, ctx);
		if (cnt != 0) {
			return responseFactory.failure("dict.suggest.err.waiting", locale);
		}

		try {
			final boolean contains = dictionaryManager.getDictionary(language).contains(word);
			switch (reclaimType) {
				case CREATE:
					if (contains) {
						return responseFactory.failure("dict.suggest.err.word.exist", locale);
					}
					ctx = new WordReclaimContext(word, null, RECLAIM_REMOVE, RESOLUTIONS_APPROVED, null);
					reclaims = dictionaryReclaimManager.searchEntities(null, ctx, RESOLUTION_ORDERS, Range.FIRST);
					if (reclaims != null && !reclaims.isEmpty()) {
						return responseFactory.failure("dict.suggest.err.word.removed", locale);
					}
					dictionaryReclaimManager.addWord(getPrincipal(), language, word, definition, attributes);
					break;
				case UPDATE:
					if (!contains) {
						return responseFactory.failure("dict.suggest.err.word.unknown", locale);
					}
					dictionaryReclaimManager.updateWord(getPrincipal(), language, word, definition, attributes);
					break;
				case REMOVE:
					if (!contains) {
						return responseFactory.failure("dict.suggest.err.word.unknown", locale);
					}
					ctx = new WordReclaimContext(word, null, RECLAIM_CREATE, RESOLUTIONS_APPROVED, null);
					reclaims = dictionaryReclaimManager.searchEntities(null, ctx, RESOLUTION_ORDERS, Range.FIRST);
					if (reclaims != null && !reclaims.isEmpty()) {
						return responseFactory.failure("dict.suggest.err.word.created", locale);
					}
					dictionaryReclaimManager.removeWord(getPrincipal(), language, word);
					break;
			}
			return responseFactory.success();
		} catch (Exception ex) {
			log.error("Word suggest can't be processed: {}", form, ex);
			return responseFactory.failure("dict.suggest.err.system", locale);
		}
	}

	@Secured("moderator")
	@RequestMapping("resolve.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse resolveService(@RequestBody WordResolutionForm form, Locale locale) {
		try {
			if ("approve".equalsIgnoreCase(form.getType())) {
				dictionaryReclaimManager.approveReclaims(form.getCommentary(), form.getIds());
			} else if ("reject".equalsIgnoreCase(form.getType())) {
				dictionaryReclaimManager.rejectReclaims(form.getCommentary(), form.getIds());
			}
		} catch (Exception ex) {
			log.error("Approval request can't be processed: {}", form, ex);
			return responseFactory.failure("dict.suggest.err.system", locale);
		}
		return responseFactory.success();
	}

	private Language getLanguage(String lang, Locale locale) {
		final Language language;
		if (lang == null) {
			language = Language.byLocale(locale);
		} else {
			language = Language.valueOf(lang.toUpperCase());
		}
		return language;
	}

	private EnumSet<WordAttribute> getWordAttributes(String[] attributes) {
		if (attributes == null || attributes.length == 0) {
			throw new IllegalArgumentException("dict.suggest.err.attributes.empty");
		}


		final WordAttribute[] attrs = new WordAttribute[attributes.length];
		for (int i = 0; i < attributes.length; i++) {
			try {
				attrs[i] = WordAttribute.valueOf(attributes[i]);
			} catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException("dict.suggest.err.attributes.unknown");
			}
		}

		final EnumSet<WordAttribute> attributes1 = WordAttribute.fromArray(attrs);
		if (!attributes1.contains(WordAttribute.FEMININE) && !attributes1.contains(WordAttribute.MASCULINE) && !attributes1.contains(WordAttribute.NEUTER)) {
			throw new IllegalArgumentException("dict.suggest.err.attributes.gender");
		}
		return attributes1;
	}

	@Autowired
	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	@Autowired
	public void setdictionaryReclaimManager(DictionaryReclaimManager dictionaryReclaimManager) {
		this.dictionaryReclaimManager = dictionaryReclaimManager;
	}
}
