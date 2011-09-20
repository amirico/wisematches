package wisematches.playground.search;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DesiredEntityDescriptor<T extends DesiredEntityBean> {
	private final Class<T> desiredEntityType;

	private final String distinctField;
	private final SearchAttribute distinctAttribute;
	private final Map<String, SearchAttribute> attributeMap;

	DesiredEntityDescriptor(Class<T> desiredEntityType) {
		this.desiredEntityType = desiredEntityType;

		final Collection<SearchAttribute> a2 = new ArrayList<SearchAttribute>();
		final Map<String, SearchAttribute> a3 = new HashMap<String, SearchAttribute>();

		final SearchDistinct d = desiredEntityType.getAnnotation(SearchDistinct.class);

		String df = null;
		SearchAttribute da = null;
		final Field[] declaredFields = desiredEntityType.getDeclaredFields();
		for (Field field : declaredFields) {
			final SearchAttribute annotation = field.getAnnotation(SearchAttribute.class);
			if (annotation != null) {
				a3.put(field.getName(), annotation);
				if (d != null && da == null && field.getName().equals(d.value())) {
					da = annotation;
					df = field.getName();
				}
			}
		}

		distinctField = df;
		distinctAttribute = da;

		attributeMap = Collections.unmodifiableMap(a3);
	}

	public SearchAttribute getAttribute(String property) {
		return attributeMap.get(property);
	}

	public SearchAttribute[] getAttributes(String... property) {
		final SearchAttribute[] res = new SearchAttribute[property.length];
		for (int i = 0; i < property.length; i++) {
			res[i] = getAttribute(property[i]);
		}
		return res;
	}

	Map<String, SearchAttribute> getAttributes() {
		return attributeMap;
	}

	String getDistinctField() {
		return distinctField;
	}

	SearchAttribute getDistinctAttribute() {
		return distinctAttribute;
	}

	public Class<T> getDesiredEntityType() {
		return desiredEntityType;
	}
}