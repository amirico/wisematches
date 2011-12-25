package wisematches.playground.tournament.impl;

import wisematches.playground.tournament.TournamentPoster;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament_poster")
public class HibernateTournamentPoster implements TournamentPoster {
    @Id
    @Column(name = "number")
    private int number;

    @Column(name = "scheduled")
    @Temporal(TemporalType.DATE)
    private Date scheduledDate;

    @Column(name = "started")
    private boolean started = false;

    @Column(name = "processed")
    private boolean processed = false;

    @Deprecated
    HibernateTournamentPoster() {
    }

    HibernateTournamentPoster(int number, Date scheduledDate) {
        this.number = number;
        this.scheduledDate = scheduledDate;
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
    public boolean isStarted() {
        return started;
    }

    void setStarted(boolean started) {
        this.started = started;
    }

    boolean isProcessed() {
        return processed;
    }

    void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "HibernateTournamentPoster{" +
                "number=" + number +
                ", scheduledDate=" + scheduledDate +
                ", started=" + started +
                '}';
    }
}
