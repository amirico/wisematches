package wisematches.server.web.controllers.playground.vocabulary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.personality.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;

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

    @RequestMapping("view")
    public String showReceivedMessage(Model model) throws UnknownEntityException {
        final Locale locale = Language.RU.locale();
        try {
            final Dictionary dictionary = dictionaryManager.getDictionary(locale);
            model.addAttribute("dictionary", dictionary);
            return "/content/playground/vocabulary/view";
        } catch (DictionaryNotFoundException ex) {
            throw new UnknownEntityException(locale, "dictionary");
        }
    }

    @Autowired
    public void setDictionaryManager(@Qualifier("lexicalDictionary") DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }
}
