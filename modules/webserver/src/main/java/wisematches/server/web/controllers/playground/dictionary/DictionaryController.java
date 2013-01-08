package wisematches.server.web.controllers.playground.dictionary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.WordAttribute;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.dictionary.form.WordApprovalForm;
import wisematches.server.web.controllers.playground.dictionary.form.WordEntryForm;
import wisematches.server.web.services.dictionary.*;

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

    private static final Log log = LogFactory.getLog("wisematches.server.dict.suggest");

    public DictionaryController() {
    }

    @Override
    public String getHeaderTitle() {
        return "dict.header";
    }

    @RequestMapping("")
    public String showDictionary(@RequestParam(value = "l", required = false) String lang, Model model, Locale locale) throws UnknownEntityException {
        final Language language = getLanguage(lang, locale);

        model.addAttribute("dictionaryLanguage", language);
        model.addAttribute("wordAttributes", WordAttribute.values());

//        final List<ChangeSuggestion> changeSuggestions = dictionarySuggestionManager.searchEntities(null, new SuggestionContext(language, EnumSet.of(SuggestionType.ADD), EnumSet.of(SuggestionState.WAITING)), null, null, Range.limit(10));
//        model.addAttribute("waitingSuggestions", changeSuggestions);

        final Dictionary dictionary = dictionaryManager.getDictionary(language);
        model.addAttribute("dictionary", dictionary);
        return "/content/playground/dictionary/view";
    }

    @RequestMapping("changes")
    public String showDictionaryChanges(@RequestParam(value = "l", required = false) String lang, Model model, Locale locale) throws UnknownEntityException {
        final Language language = getLanguage(lang, locale);

        final SuggestionContext ctx = new SuggestionContext(language, null, EnumSet.of(SuggestionState.WAITING));

        final List<ChangeSuggestion> waitingSuggestions = dictionarySuggestionManager.searchEntities(null, ctx, null, null, null);
        model.addAttribute("waitingSuggestions", waitingSuggestions);

        // TODO: must be changed according to use role.
        model.addAttribute("moderator", isModerator());

        return "/content/playground/dictionary/changes";
    }

    @ResponseBody
    @RequestMapping("loadWordEntry.ajax")
    public ServiceResponse loadWordEntry(@RequestParam("l") String lang, @RequestParam("w") String word, Locale locale) {
        final Language language;
        try {
            language = Language.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.language", locale));
        }
        final Dictionary dictionary = dictionaryManager.getDictionary(language);
        if (dictionary == null) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.dictionary", locale));
        }
        return ServiceResponse.success(null, "wordEntry", dictionary.getWordEntry(word.toLowerCase()));
    }

    @ResponseBody
    @RequestMapping("loadWordEntries.ajax")
    public ServiceResponse loadWordEntries(@RequestParam("l") String lang, @RequestParam("p") String prefix, Locale locale) {
        final Language language;
        try {
            language = Language.valueOf(lang.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.language", locale));
        }
        final Dictionary dictionary = dictionaryManager.getDictionary(language);
        if (dictionary == null) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.dictionary", locale));
        }
        return ServiceResponse.success(null, "wordEntries", dictionary.getWordEntries(prefix.toLowerCase()));
    }

    @ResponseBody
    @RequestMapping("editWordEntry.ajax")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServiceResponse createWord(@RequestBody WordEntryForm form, Locale locale) {
        Language language;
        try {
            language = Language.valueOf(form.getLanguage().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.language", locale));
        }

        final SuggestionType suggestionType;
        try {
            suggestionType = SuggestionType.valueOf(form.getAction().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.action", locale));
        }

        final String word = form.getWord().toLowerCase();
        if (word.length() < 2) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.short", locale));
        }

        if (!language.getAlphabet().validate(word)) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.alphabet", locale));
        }

        EnumSet<WordAttribute> attributes1 = null;
        if (suggestionType != SuggestionType.REMOVE) {
            final String[] attributes = form.getAttributes();
            if (attributes == null || attributes.length == 0) {
                return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.attributes.empty", locale));
            }


            final WordAttribute[] attrs = new WordAttribute[attributes.length];
            for (int i = 0; i < attributes.length; i++) {
                try {
                    attrs[i] = WordAttribute.valueOf(attributes[i]);
                } catch (IllegalArgumentException ex) {
                    return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.attributes.unknown", locale));
                }
            }

            attributes1 = WordAttribute.fromArray(attrs);
            if (!attributes1.contains(WordAttribute.FEMININE) && !attributes1.contains(WordAttribute.MASCULINE) && !attributes1.contains(WordAttribute.NEUTER)) {
                return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.attributes.gender", locale));
            }
        }

        final int cnt = dictionarySuggestionManager.getTotalCount(null, new SuggestionContext(word, null, null, EnumSet.of(SuggestionState.WAITING)));
        if (cnt != 0) {
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.waiting", locale));
        }

        try {
            final boolean contains = dictionaryManager.getDictionary(language).contains(word);
            switch (suggestionType) {
                case ADD:
                    if (contains) {
                        return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.word.exist", locale));
                    }
                    dictionarySuggestionManager.addWord(word, form.getDefinition(), attributes1, language, getPersonality());
                    break;
                case UPDATE:
                    if (!contains) {
                        return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.word.unknown", locale));
                    }
                    dictionarySuggestionManager.updateWord(word, form.getDefinition(), attributes1, language, getPersonality());
                    break;
                case REMOVE:
                    if (!contains) {
                        return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.word.unknown", locale));
                    }
                    dictionarySuggestionManager.removeWord(word, language, getPersonality());
                    break;
            }
            return ServiceResponse.success();
        } catch (Exception ex) {
            log.error("Word suggest can't be processed: " + form, ex);
            return ServiceResponse.failure(gameMessageSource.getMessage("dict.suggest.err.system", locale));
        }
    }

    @ResponseBody
    @RequestMapping("processChangeRequest.ajax")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ServiceResponse processChangeRequest(@RequestBody WordApprovalForm form, Locale locale) {
        if (!isModerator()) {
            return ServiceResponse.failure("You are not moderator!");
        }
        try {
            if ("approve".equalsIgnoreCase(form.getType())) {
                dictionarySuggestionManager.approveRequests(form.getIds());
            } else if ("reject".equalsIgnoreCase(form.getType())) {
                dictionarySuggestionManager.rejectRequests(form.getIds());
            }
        } catch (Exception ex) {
            log.error("Approval request can't be processed: " + form, ex);
            return ServiceResponse.failure(ex.getMessage());
        }
        return ServiceResponse.success();
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

    private boolean isModerator() {
        final String nickname = getPrincipal().getNickname();
        return nickname.equals("smklimenko") || nickname.equals("test");
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