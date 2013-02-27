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
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.server.services.dictionary.*;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.dictionary.form.WordApprovalForm;
import wisematches.server.web.servlet.mvc.playground.dictionary.form.WordDefinitionForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/dictionary")
public class DictionaryController extends WisematchesController {
	private DictionaryManager dictionaryManager;
	private DictionarySuggestionManager dictionarySuggestionManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.DictionaryController");

	public DictionaryController() {
	}

	@RequestMapping("")
	public String showDictionaryPage(@RequestParam(value = "l", required = false) String lang, Model model, Locale locale) throws UnknownEntityException {
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

	@RequestMapping("changes")
	public String showDictionaryChangesPage(@RequestParam(value = "l", required = false) String lang, Model model, Locale locale) throws UnknownEntityException {
		final Language language = getLanguage(lang, locale);
		final SuggestionContext ctx = new SuggestionContext(language, null, EnumSet.of(SuggestionState.WAITING));

		final List<ChangeSuggestion> suggestions = dictionarySuggestionManager.searchEntities(null, ctx, null, null);
		model.addAttribute("changeSuggestion", suggestions);
		return "/content/playground/dictionary/changes";
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

	@RequestMapping("editWordEntry.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse createWordService(@RequestBody WordDefinitionForm form, Locale locale) {
		Language language;
		try {
			language = Language.valueOf(form.getLanguage().toUpperCase());
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure("dict.suggest.err.language", locale);
		}

		final SuggestionType suggestionType;
		try {
			suggestionType = SuggestionType.valueOf(form.getAction().toUpperCase());
		} catch (IllegalArgumentException ex) {
			return responseFactory.failure("dict.suggest.err.action", locale);
		}

		final String word = form.getWord().toLowerCase();
		if (word.length() < 2) {
			return responseFactory.failure("dict.suggest.err.short", locale);
		}

		if (!language.getAlphabet().validate(word)) {
			return responseFactory.failure("dict.suggest.err.alphabet", locale);
		}

		EnumSet<WordAttribute> attributes1 = null;
		if (suggestionType != SuggestionType.REMOVE) {
			final String[] attributes = form.getAttributes();
			if (attributes == null || attributes.length == 0) {
				return responseFactory.failure("dict.suggest.err.attributes.empty", locale);
			}


			final WordAttribute[] attrs = new WordAttribute[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				try {
					attrs[i] = WordAttribute.valueOf(attributes[i]);
				} catch (IllegalArgumentException ex) {
					return responseFactory.failure("dict.suggest.err.attributes.unknown", locale);
				}
			}

			attributes1 = WordAttribute.fromArray(attrs);
			if (!attributes1.contains(WordAttribute.FEMININE) && !attributes1.contains(WordAttribute.MASCULINE) && !attributes1.contains(WordAttribute.NEUTER)) {
				return responseFactory.failure("dict.suggest.err.attributes.gender", locale);
			}
		}

		final int cnt = dictionarySuggestionManager.getTotalCount(null, new SuggestionContext(word, null, null, EnumSet.of(SuggestionState.WAITING)));
		if (cnt != 0) {
			return responseFactory.failure("dict.suggest.err.waiting", locale);
		}

		try {
			final boolean contains = dictionaryManager.getDictionary(language).contains(word);
			switch (suggestionType) {
				case ADD:
					if (contains) {
						return responseFactory.failure("dict.suggest.err.word.exist", locale);
					}
					dictionarySuggestionManager.addWord(word, form.getDefinition(), attributes1, language, getPrincipal());
					break;
				case UPDATE:
					if (!contains) {
						return responseFactory.failure("dict.suggest.err.word.unknown", locale);
					}
					dictionarySuggestionManager.updateWord(word, form.getDefinition(), attributes1, language, getPrincipal());
					break;
				case REMOVE:
					if (!contains) {
						return responseFactory.failure("dict.suggest.err.word.unknown", locale);
					}
					dictionarySuggestionManager.removeWord(word, language, getPrincipal());
					break;
			}
			return responseFactory.success();
		} catch (Exception ex) {
			log.error("Word suggest can't be processed: {}", form, ex);
			return responseFactory.failure("dict.suggest.err.system", locale);
		}
	}

	@Secured("moderator")
	@RequestMapping("resolveWordEntry.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse processChangeRequestService(@RequestBody WordApprovalForm form, Locale locale) {
		try {
			if ("approve".equalsIgnoreCase(form.getType())) {
				dictionarySuggestionManager.approveRequests(form.getIds());
			} else if ("reject".equalsIgnoreCase(form.getType())) {
				dictionarySuggestionManager.rejectRequests(form.getIds());
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

	@Autowired
	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	@Autowired
	public void setDictionarySuggestionManager(DictionarySuggestionManager dictionarySuggestionManager) {
		this.dictionarySuggestionManager = dictionarySuggestionManager;
	}
}
