package wisematches.playground.tourney.regular.impl;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
abstract class HibernateRegularTourneyEntity {
	@Column(name = "started")
	private Date startedDate;

	@Column(name = "finished")
	private Date finishedDate;

	HibernateRegularTourneyEntity() {
	}

	public Date getStartedDate() {
		return startedDate;
	}

	public Date getFinishedDate() {
		return finishedDate;
	}

	void startTourney() {
		if (startedDate != null) {
			throw new IllegalArgumentException("Already started of finished");
		}
		startedDate = new Date();
	}

	void finishTourney() {
		if (startedDate == null) {
			throw new IllegalArgumentException("Is not started yet");
		}
		if (finishedDate != null) {
			throw new IllegalArgumentException("Already started of finished");
		}
		finishedDate = new Date();
	}
}
