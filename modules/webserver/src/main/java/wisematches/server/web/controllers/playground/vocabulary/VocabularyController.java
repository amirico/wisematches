package wisematches.server.web.controllers.playground.vocabulary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.playground.dictionary.IterableDictionary;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.vocabulary.view.VocabularyDistribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/vocabulary")
public class VocabularyController extends WisematchesController {
    private DictionaryManager dictionaryManager;

    public VocabularyController() {
    }

    @RequestMapping("")
    public String showReceivedMessage(@RequestParam("p") String prefix, Model model) throws UnknownEntityException {
        final Language language = Language.RU;
        final Locale locale = language.locale();
        try {
            final IterableDictionary dictionary = (IterableDictionary) dictionaryManager.getDictionary(locale);
            model.addAttribute("words", loadWords("ru", prefix, model));
            model.addAttribute("distribution", new VocabularyDistribution(dictionary));
            return "/content/playground/vocabulary/view";
        } catch (DictionaryNotFoundException ex) {
            throw new UnknownEntityException(language, "dictionary");
        }
    }

    @ResponseBody
    @RequestMapping("load.ajax")
    public Collection<String> loadWords(
            @RequestParam("l") String lang, @RequestParam("p") String prefix, Model model) throws UnknownEntityException {
        final Language language = Language.byCode(lang.toUpperCase());


        try {
            prefix = prefix.toLowerCase();
            final IterableDictionary dictionary = (IterableDictionary) dictionaryManager.getDictionary(language.locale());
            Collection<String> strings = new ArrayList<>();
            for (String word : dictionary) {
                if (word.startsWith(prefix)) {
                    strings.add(word);
                }
            }
            return strings;
        } catch (DictionaryNotFoundException ex) {
            throw new UnknownEntityException(language, "dictionary");
        }
    }


    @Autowired
    public void setDictionaryManager(@Qualifier("lexicalDictionary") DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }
}
