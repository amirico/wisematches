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

	protected HibernateBoardSettings(long playerId, boolean cleanMemory, boolean checkWords, String tilesClass) {
		super(cleanMemory, checkWords, tilesClass);
		this.playerId = playerId;
	}

	protected void update(BoardSettings s) {
		setCheckWords(s.isCheckWords());
		setCleanMemory(s.isCleanMemory());
		setTilesClass(s.getTilesClass());
	}

	public long getPlayerId() {
		return playerId;
	}
}
