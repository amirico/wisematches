package wisematches.server.web.servlet.mvc.playground.scribble.settings;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.core.Player;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.server.web.servlet.mvc.playground.scribble.AbstractScribbleController;
import wisematches.server.web.servlet.mvc.playground.scribble.settings.form.BoardSettingsForm;
import wisematches.server.web.servlet.sdo.ServiceResponse;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/playground/scribble/settings")
public class ScribbleSettingsController extends AbstractScribbleController {
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


	@RequestMapping("save.ajax")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ServiceResponse saveBoardSettings(@RequestBody final BoardSettingsForm form) {
		final BoardSettings settings = new BoardSettings(form.isCleanMemory(), form.isCheckWords(), form.isClearByClick(), form.isShowCaptions(), form.isEnableShare(), form.getTilesClass());
		boardSettingsManager.setScribbleSettings(getPrincipal(), settings);
		return responseFactory.success(form);
	}
}
