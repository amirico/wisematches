package wisematches.server.web.controllers.playground.tourney.form;

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
		return g != null;
	}

	public boolean isRound() {
		return r != null && !isGroup();
	}

	public boolean isDivision() {
		return s != null && l != null && !isRound();
	}

	public boolean isTourney() {
		return t != null && !isDivision();
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