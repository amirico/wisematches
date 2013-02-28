package wisematches.server.web.servlet.sdo.table;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class ColumnDescriptor {
	private final String name;
	private final boolean sortable;

	public ColumnDescriptor(String name, boolean sortable) {
		this.name = name;
		this.sortable = sortable;
	}

	public String getName() {
		return name;
	}

	public boolean isSortable() {
		return sortable;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("ColumnDescriptor{");
		sb.append("name='").append(name).append('\'');
		sb.append(", sortable=").append(sortable);
		sb.append('}');
		return sb.toString();
	}
}
