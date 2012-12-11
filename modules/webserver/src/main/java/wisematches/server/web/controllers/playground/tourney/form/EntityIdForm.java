package wisematches.server.web.controllers.playground.tourney.form;

import wisematches.personality.Language;
import wisematches.playground.tourney.regular.*;
import wisematches.server.web.controllers.UnknownEntityException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class EntityIdForm {
	private String t; // tourney
	private String l; // language
	private String s; // section
	private String r; // round
	private String g; // group

	public EntityIdForm() {
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getR() {
		return r;
	}

	public void setR(String r) {
		this.r = r;
	}

	public String getG() {
		return g;
	}

	public void setG(String g) {
		this.g = g;
	}

	public boolean isShit() {
		return t == null;
	}

	public boolean isGroup() {
		return t != null && s != null && l != null && r == null && g != null;
	}

	public boolean isRound() {
		return t != null && s != null && l != null && r != null && g == null;
	}

	public boolean isDivision() {
		return t != null && s != null && l != null && r == null && g == null;
	}

	public boolean isTourney() {
		return t != null && s == null && l == null && r == null && g == null;
	}

	public Tourney.Id getTourneyId() throws UnknownEntityException {
		try {
			return new Tourney.Id(Integer.parseInt(t));
		} catch (Exception ex) {
			throw new UnknownEntityException(this, "tourney");
		}
	}

	public TourneyDivision.Id getDivisionId() throws UnknownEntityException {
		try {
			final Language language = Language.values()[Integer.parseInt(l)];
			final TourneySection section = TourneySection.values()[Integer.parseInt(s)];
			return new TourneyDivision.Id(getTourneyId(), language, section);
		} catch (Exception ex) {
			throw new UnknownEntityException(this, "tourney");
		}
	}

	public TourneyRound.Id getRoundId() throws UnknownEntityException {
		try {
			return new TourneyRound.Id(getDivisionId(), Integer.parseInt(r));
		} catch (Exception ex) {
			throw new UnknownEntityException(this, "tourney");
		}
	}

	public TourneyGroup.Id getGroupId() throws UnknownEntityException {
		try {
			return new TourneyGroup.Id(getRoundId(), Integer.parseInt(g));
		} catch (Exception ex) {
			throw new UnknownEntityException(this, "tourney");
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("EntityIdForm");
		sb.append("{t='").append(t).append('\'');
		sb.append(", l='").append(l).append('\'');
		sb.append(", s='").append(s).append('\'');
		sb.append(", r='").append(r).append('\'');
		sb.append(", g='").append(g).append('\'');
		sb.append('}');
		return sb.toString();
	}
}