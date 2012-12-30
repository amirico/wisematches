package wisematches.server.web.controllers.playground.vocabulary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.WordEntry;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;

import java.util.Collection;

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
    public String showReceivedMessage(Model model) throws UnknownEntityException {
        final Language language = Language.RU;


        final Dictionary dictionary = dictionaryManager.getDictionary(language);
        model.addAttribute("vocabularies", dictionary.getVocabularies());
        return "/content/playground/vocabulary/view";
    }

    @ResponseBody
    @RequestMapping("load.ajax")
    public Collection<WordEntry> loadWords(@RequestParam("v") String vid,
                                           @RequestParam("p") String prefix) throws UnknownEntityException {
        final Dictionary dictionary = dictionaryManager.getDictionary(Language.RU);
        return dictionary.getWordEntries(prefix);
    }

    @Autowired
    public void setDictionaryManager(DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }
}
