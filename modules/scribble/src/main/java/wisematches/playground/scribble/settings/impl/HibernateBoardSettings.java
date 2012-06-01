package wisematches.playground.scribble.settings.impl;

import wisematches.playground.scribble.settings.BoardSettings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "scribble_settings")
public class HibernateBoardSettings extends BoardSettings {
	@Id
	@Column(name = "playerId")
	private long playerId;

	protected HibernateBoardSettings() {
	}

	protected HibernateBoardSettings(long playerId, boolean cleanMemory, boolean checkWords, boolean clearByClick, boolean showCaptions, String tilesClass) {
		super(cleanMemory, checkWords, clearByClick, showCaptions, tilesClass);
		this.playerId = playerId;
	}

	protected void update(BoardSettings s) {
		setCheckWords(s.isCheckWords());
		setCleanMemory(s.isCleanMemory());
		setClearByClick(s.isClearByClick());
		setTilesClass(s.getTilesClass());
		setShowCaptions(s.isShowCaptions());
	}

	public long getPlayerId() {
		return playerId;
	}
}
