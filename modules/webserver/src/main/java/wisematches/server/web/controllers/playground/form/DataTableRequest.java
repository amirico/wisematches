package wisematches.server.web.controllers.playground.form;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataTableRequest {
	private int iDisplayStart;
	private int iDisplayLength;
	private String sEcho;
	private int iColumns;
	private String sSearch;
	private boolean bRegex;

	public DataTableRequest() {
	}

	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	public int getiColumns() {
		return iColumns;
	}

	public void setiColumns(int iColumns) {
		this.iColumns = iColumns;
	}

	public String getsSearch() {
		return sSearch;
	}

	public void setsSearch(String sSearch) {
		this.sSearch = sSearch;
	}

	public boolean isbRegex() {
		return bRegex;
	}

	public void setbRegex(boolean bRegex) {
		this.bRegex = bRegex;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("DataTableRequest");
		sb.append("{bRegex=").append(bRegex);
		sb.append(", iDisplayLength=").append(iDisplayLength);
		sb.append(", sEcho='").append(sEcho).append('\'');
		sb.append(", iColumns=").append(iColumns);
		sb.append(", sSearch='").append(sSearch).append('\'');
		sb.append(", iDisplayStart=").append(iDisplayStart);
		sb.append('}');
		return sb.toString();
	}
}
