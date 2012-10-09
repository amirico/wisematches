package wisematches.server.web.controllers.playground.scribble.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardSettingsForm {
	private String tilesClass;
	private boolean checkWords;
	private boolean cleanMemory;
	private boolean clearByClick;
	private boolean showCaptions;

	public BoardSettingsForm() {
	}

	public String getTilesClass() {
		return tilesClass;
	}

	public void setTilesClass(String tilesClass) {
		this.tilesClass = tilesClass;
	}

	public boolean isCheckWords() {
		return checkWords;
	}

	public void setCheckWords(boolean checkWords) {
		this.checkWords = checkWords;
	}

	public boolean isCleanMemory() {
		return cleanMemory;
	}

	public void setCleanMemory(boolean cleanMemory) {
		this.cleanMemory = cleanMemory;
	}

	public boolean isClearByClick() {
		return clearByClick;
	}

	public void setClearByClick(boolean clearByClick) {
		this.clearByClick = clearByClick;
	}

	public boolean isShowCaptions() {
		return showCaptions;
	}

	public void setShowCaptions(boolean showCaptions) {
		this.showCaptions = showCaptions;
	}
}
