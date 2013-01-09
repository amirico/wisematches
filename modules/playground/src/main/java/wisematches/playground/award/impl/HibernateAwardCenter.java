package wisematches.playground.award.impl;

import org.hibernate.SessionFactory;
import wisematches.playground.BoardManager;
import wisematches.playground.tourney.regular.RegularTourneyManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAwardCenter {
	private BoardManager boardManager;
	private SessionFactory sessionFactory;
	private RegularTourneyManager tourneyManager;

	public HibernateAwardCenter() {
	}


	public void setBoardManager(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setTourneyManager(RegularTourneyManager tourneyManager) {
		this.tourneyManager = tourneyManager;
	}
}