package wisematches.playground.search;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockDesiredEntity implements DesiredEntity {
	@SearchAttribute(column = "account.id", sortable = false)
	private long pid;

	@SearchAttribute(column = "account.nickname")
	private String nickname;

	@SearchAttribute(column = "profile.language")
	private Language language;

	public MockDesiredEntity() {
	}

	public long getPid() {
		return pid;
	}

	public String getNickname() {
		return nickname;
	}

	public Language getLanguage() {
		return language;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
}
