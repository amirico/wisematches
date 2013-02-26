package wisematches.server.web.servlet.mvc.playground.relations.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
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
