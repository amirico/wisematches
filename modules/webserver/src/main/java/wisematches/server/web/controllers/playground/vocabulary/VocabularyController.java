package wisematches.server.web.controllers.playground.vocabulary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.WisematchesController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/vocabulary")
public class VocabularyController extends WisematchesController {
/*
	private VocabularyManagerOld vocabularyManager;

	public VocabularyController() {
	}

	@RequestMapping("")
	public String showReceivedMessage(Model model) throws UnknownEntityException {
		final Language language = Language.RU;

		model.addAttribute("vocabularies", vocabularyManager.getVocabularies(language));
		return "/content/playground/vocabulary/view";
	}

	@ResponseBody
	@RequestMapping("load.ajax")
	public Collection<Word> loadWords(@RequestParam("v") String vid,
									  @RequestParam("p") String prefix) throws UnknownEntityException {
		final VocabularyOld vocabulary = vocabularyManager.getVocabulary(vid);
		return vocabulary.searchWords(prefix);
	}

	@Autowired
	public void setVocabularyManager(VocabularyManagerOld vocabularyManager) {
		this.vocabularyManager = vocabularyManager;
	}
*/
}
