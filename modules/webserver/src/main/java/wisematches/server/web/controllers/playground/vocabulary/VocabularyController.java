package wisematches.server.web.controllers.playground.vocabulary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.personality.Language;
import wisematches.playground.vocabulary.Vocabulary;
import wisematches.playground.vocabulary.VocabularyManager;
import wisematches.playground.vocabulary.Word;
import wisematches.server.web.controllers.UnknownEntityException;
import wisematches.server.web.controllers.WisematchesController;
import wisematches.server.web.controllers.playground.vocabulary.view.VocabularyDistribution;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/vocabulary")
public class VocabularyController extends WisematchesController {
	private VocabularyManager vocabularyManager;

	public VocabularyController() {
	}

	@RequestMapping("")
	public String showReceivedMessage(Model model) throws UnknownEntityException {
		final Language language = Language.RU;

		final Vocabulary next = vocabularyManager.getVocabularies(language).iterator().next();
		model.addAttribute("vocabulary", next);
		model.addAttribute("distribution", new VocabularyDistribution(next));
		return "/content/playground/vocabulary/view";
	}

	@ResponseBody
	@RequestMapping("load.ajax")
	public Collection<Word> loadWords(
			@RequestParam("l") String lang, @RequestParam("p") String prefix, Model model) throws UnknownEntityException {
		final Language language = Language.byCode(lang.toUpperCase());

		final Vocabulary next = vocabularyManager.getVocabularies(language).iterator().next();
		return next.searchWords(prefix);
	}

	@Autowired
	public void setVocabularyManager(VocabularyManager vocabularyManager) {
		this.vocabularyManager = vocabularyManager;
	}
}
