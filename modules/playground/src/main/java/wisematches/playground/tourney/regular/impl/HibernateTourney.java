package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.Tourney;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular")
public class HibernateTourney implements Tourney {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long internalId;

	@Column(name = "tourneyNumber")
	private int number;

	@Column(name = "scheduled")
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledDate;

	@Column(name = "started")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startedDate;

	@Column(name = "finished")
	@Temporal(TemporalType.TIMESTAMP)
	private Date finishedDate;

	@Deprecated
	private HibernateTourney() {
	}

	public HibernateTourney(int number, Date scheduledDate) {
		if (number < 0) {
			throw new IllegalArgumentException("Incorrect tournament number: " + number);
		}
		if (scheduledDate == null) {
			throw new NullPointerException("Scheduled date can't be null");
		}
//		if (scheduledDate.getTime() < System.currentTimeMillis()) {
//			throw new IllegalArgumentException("Scheduled date can't be in past.");
//		}
		this.number = number;
		this.scheduledDate = scheduledDate;
	}

	long getInternalId() {
		return internalId;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public State getState() {
		return State.getState(startedDate, finishedDate);
	}

	@Override
	public Date getStartedDate() {
		return startedDate;
	}

	@Override
	public Date getFinishedDate() {
		return finishedDate;
	}

	@Override
	public Date getScheduledDate() {
		return scheduledDate;
	}

	@Override
	public Tourney.Id getId() {
		return new Tourney.Id(number);
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

	@Override
	public String toString() {
		return "HibernateTourney{" +
				"internalId=" + internalId +
				", tourney=" + number +
				", scheduledDate=" + scheduledDate +
				", startedDate=" + startedDate +
				", finishedDate=" + finishedDate +
				'}';
	}
}
