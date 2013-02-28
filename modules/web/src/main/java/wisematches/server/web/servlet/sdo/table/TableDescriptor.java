package wisematches.server.web.servlet.sdo.table;

import wisematches.core.search.descriptive.SearchableDescriptor;
import wisematches.core.search.descriptive.SearchableProperty;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class TableDescriptor {
	private final List<ColumnDescriptor> columnDescriptors;

	public TableDescriptor(SearchableDescriptor descriptor) {
		final Set<String> propertyNames = descriptor.getPropertyNames();
		columnDescriptors = new ArrayList<>(propertyNames.size());

		for (String name : propertyNames) {
			final SearchableProperty property = descriptor.getProperty(name);
			columnDescriptors.add(new ColumnDescriptor(name, property.sortable()));
		}
	}

	public TableDescriptor(SearchableDescriptor descriptor, String... propertyNames) {
		columnDescriptors = new ArrayList<>(propertyNames.length);
		for (String name : propertyNames) {
			final SearchableProperty property = descriptor.getProperty(name);
			columnDescriptors.add(new ColumnDescriptor(name, property.sortable()));
		}
	}

	public TableDescriptor(ColumnDescriptor[] columnDescriptors) {
		this(Arrays.asList(columnDescriptors));
	}

	public TableDescriptor(List<ColumnDescriptor> columnDescriptors) {
		this.columnDescriptors = Collections.unmodifiableList(columnDescriptors);
	}

	public ColumnDescriptor getColumnDescriptor(int pos) {
		return columnDescriptors.get(pos);
	}

	public ColumnDescriptor getColumnDescriptor(String name) {
		for (ColumnDescriptor columnDescriptor : columnDescriptors) {
			if (columnDescriptor.getName().equals(name)) {
				return columnDescriptor;
			}
		}
		return null;
	}

	public List<ColumnDescriptor> getColumnDescriptors() {
		return columnDescriptors;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TableDescriptor{");
		sb.append("columnDescriptors=").append(columnDescriptors);
		sb.append('}');
		return sb.toString();
	}
}
