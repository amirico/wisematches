package wisematches.server.web.servlet.mvc.playground.dictionary.form;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordResolutionForm {
	private Long[] ids;
	private String type;
	private String commentary;

	public WordResolutionForm() {
	}

	public Long[] getIds() {
		return ids;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	@Override
	public String toString() {
		return "WordApprovalForm{" +
				"type='" + type + '\'' +
				", ids=" + Arrays.toString(ids) +
				'}';
	}
}
