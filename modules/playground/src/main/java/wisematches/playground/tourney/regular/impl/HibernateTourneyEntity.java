package wisematches.playground.tourney.regular.impl;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
abstract class HibernateTourneyEntity {
	@Column(name = "started")
	protected Date startedDate;

	@Column(name = "finished")
	protected Date finishedDate;

	@Column(name = "lastChange")
	protected Date lastChange;

	HibernateTourneyEntity() {
		lastChange = new Date();
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public Date getFinishedDate() {
		return finishedDate;
	}

	Date getLastChange() {
		return lastChange;
	}

	void markStarted() {
		if (startedDate != null) {
			throw new IllegalArgumentException("Already started of finished");
		}
		startedDate = new Date();
		invalidate();
	}

	void markFinished() {
		if (startedDate == null) {
			throw new IllegalArgumentException("Is not started yet");
		}
		if (finishedDate != null) {
			throw new IllegalArgumentException("Already started of finished");
		}
		finishedDate = new Date();
		invalidate();
	}

	/**
	 * Indicates that state of the entity was changed.
	 */
	protected void invalidate() {
		lastChange = new Date();
	}
}
