package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.RegularTourney;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular")
public class HibernateTourney implements RegularTourney {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

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

	@Column(name = "lastChange")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastChange;

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
		this.lastChange = new Date();
	}

	long getDbId() {
		return id;
	}

	@Override
	public int getNumber() {
		return number;
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
	public RegularTourney.Id getId() {
		return new RegularTourney.Id(number);
	}

	void startTourney() {
		if (startedDate != null) {
			throw new IllegalArgumentException("Already started of finished");
		}
		lastChange = startedDate = new Date();
	}

	void finishTourney() {
		if (startedDate == null) {
			throw new IllegalArgumentException("Is not started yet");
		}
		if (finishedDate != null) {
			throw new IllegalArgumentException("Already started of finished");
		}
		lastChange = finishedDate = new Date();
	}

	@Override
	public String toString() {
		return "HibernateTourney{" +
				"id=" + id +
				", tourney=" + number +
				", scheduledDate=" + scheduledDate +
				", startedDate=" + startedDate +
				", finishedDate=" + finishedDate +
				", lastChange=" + lastChange +
				'}';
	}
}
