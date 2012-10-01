package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.regular.RegularTourney;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tourney_regular")
public class HibernateTourney extends HibernateTourneyEntity implements RegularTourney {
	@Column(name = "id")
	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "number")
	private int number;

	@Column(name = "started")
	protected Date startedDate;

	@Column(name = "finished")
	protected Date finishedDate;

	@Column(name = "lastChange")
	protected Date lastChange;

	@Column(name = "scheduled")
	private Date scheduledDate;

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
		if (scheduledDate.getTime() < System.currentTimeMillis()) {
			throw new IllegalArgumentException("Scheduled date can't be in past.");
		}
		this.number = number;
		this.scheduledDate = scheduledDate;
	}

	long getDbId() {
		return id;
	}


	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Date getScheduledDate() {
		return scheduledDate;
	}

	@Override
	public RegularTourney.Id getId() {
		return new RegularTourney.Id(number);
	}

	@Override
	public String toString() {
		return "HibernateTourney{" +
				"number=" + number +
				", scheduledDate=" + scheduledDate +
				'}';
	}
}
