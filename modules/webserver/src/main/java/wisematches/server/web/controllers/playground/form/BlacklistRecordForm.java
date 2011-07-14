package wisematches.server.web.controllers.playground.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BlacklistRecordForm {
	private long person;
	private String comment;

	public BlacklistRecordForm() {
	}

	public long getPerson() {
		return person;
	}

	public void setPerson(long person) {
		this.person = person;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
