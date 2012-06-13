package wisematches.playground.tournament.impl.tournament;

import wisematches.playground.tournament.Announcement;
import wisematches.playground.tournament.Tournament;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "tournament")
public class HibernateTournament implements Tournament {
    @Id
    @Column(name = "id")
    private int number;

    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "finishDate")
    @Temporal(TemporalType.DATE)
    private Date finishDate;

    @Deprecated
    HibernateTournament() {
    }

    public HibernateTournament(Announcement announcement) {
        this.number = announcement.getNumber();
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public Date getFinishDate() {
        return finishDate;
    }

    @Override
    public boolean isStarted() {
        return startDate != null;
    }

    @Override
    public boolean isFinished() {
        return finishDate != null;
    }
}
