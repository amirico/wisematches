package wisematches.server.web.servlet.mvc.playground.scribble.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wisematches.core.Player;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.server.web.servlet.mvc.DeprecatedResponse;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.scribble.settings.form.BoardSettingsForm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/settings")
@Deprecated
public class ScribbleSettingsController extends WisematchesController {
	private BoardSettingsManager boardSettingsManager;

	public ScribbleSettingsController() {
	}

	@RequestMapping("load")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public String loadBoardSettings(final Model model, @ModelAttribute("settings") final BoardSettingsForm form) {
		final Player principal = getPrincipal();

		final BoardSettings settings = boardSettingsManager.getScribbleSettings(principal);
		form.setTilesClass(settings.getTilesClass());
		form.setCheckWords(settings.isCheckWords());
		form.setCleanMemory(settings.isCleanMemory());
		form.setClearByClick(settings.isClearByClick());
		form.setShowCaptions(settings.isShowCaptions());
		form.setEnableShare(settings.isEnableShare());

		model.addAttribute("plain", Boolean.TRUE);

		return "/content/playground/scribble/settings";
	}


	@ResponseBody
	@RequestMapping("save")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DeprecatedResponse saveBoardSettings(final Model model, @ModelAttribute("settings") final BoardSettingsForm form) {
		final BoardSettings settings = new BoardSettings(form.isCleanMemory(), form.isCheckWords(), form.isClearByClick(), form.isShowCaptions(), form.isEnableShare(), form.getTilesClass());
		boardSettingsManager.setScribbleSettings(getPrincipal(), settings);
		return DeprecatedResponse.success(null, "settings", form);
	}

	@Autowired
	public void setBoardSettingsManager(BoardSettingsManager boardSettingsManager) {
		this.boardSettingsManager = boardSettingsManager;
	}
}
