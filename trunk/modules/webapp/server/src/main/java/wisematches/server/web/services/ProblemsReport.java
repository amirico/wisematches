/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.services;

/**
 * @author klimese
 */
public class ProblemsReport {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ProblemsReport");
		sb.append("{name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
