package wisematches.playground.scribble.settings;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class BoardSettings implements Cloneable {
	@Column(name = "cleanMemory")
	private boolean cleanMemory = true;

	@Column(name = "checkWords")
	private boolean checkWords = true;

	@Column(name = "clearByClick")
	private boolean clearByClick = true;

	@Column(name = "showCaptions")
	private boolean showCaptions = true;

	@Column(name = "enableShare")
	private boolean enableShare = true;

	@Column(name = "tilesClass")
	private String tilesClass;

	public BoardSettings() {
	}

	public BoardSettings(boolean cleanMemory, boolean checkWords, boolean clearByClick, boolean showCaptions, boolean enableShare, String tilesClass) {
		this.cleanMemory = cleanMemory;
		this.checkWords = checkWords;
		this.clearByClick = clearByClick;
		this.showCaptions = showCaptions;
		this.enableShare = enableShare;
		this.tilesClass = tilesClass;
	}

	public boolean isCleanMemory() {
		return cleanMemory;
	}

	public boolean isCheckWords() {
		return checkWords;
	}

	public String getTilesClass() {
		return tilesClass;
	}

	public void setCleanMemory(boolean cleanMemory) {
		this.cleanMemory = cleanMemory;
	}

	public void setCheckWords(boolean checkWords) {
		this.checkWords = checkWords;
	}

	public void setTilesClass(String tilesClass) {
		this.tilesClass = tilesClass;
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

	public boolean isEnableShare() {
		return enableShare;
	}

	public void setEnableShare(boolean enableShare) {
		this.enableShare = enableShare;
	}

	@Override
	public BoardSettings clone() {
		try {
			return (BoardSettings) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException("Clone is not supported", e);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("BoardSettings");
		sb.append("{cleanMemory=").append(cleanMemory);
		sb.append(", checkWords=").append(checkWords);
		sb.append(", clearByClick=").append(clearByClick);
		sb.append(", showCaptions=").append(showCaptions);
		sb.append(", enableShare=").append(enableShare);
		sb.append(", tilesClass='").append(tilesClass).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
