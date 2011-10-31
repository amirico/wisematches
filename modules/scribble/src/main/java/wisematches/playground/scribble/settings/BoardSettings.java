package wisematches.playground.scribble.settings;

import javax.persistence.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class BoardSettings implements Cloneable {
	@Column(name = "cleanMemory")
	private boolean cleanMemory = true;

	@Column(name = "checkWords")
	private boolean checkWords = true;

	@Column(name = "tilesClass")
	private String tilesClass;

	public BoardSettings() {
	}

	public BoardSettings(boolean cleanMemory, boolean checkWords, String tilesClass) {
		this.cleanMemory = cleanMemory;
		this.checkWords = checkWords;
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
		return "BoardSettings{" +
				", cleanMemory=" + cleanMemory +
				", checkWords=" + checkWords +
				", tilesClass='" + tilesClass + '\'' +
				'}';
	}
}
