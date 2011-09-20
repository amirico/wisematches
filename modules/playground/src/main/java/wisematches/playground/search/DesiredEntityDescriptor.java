package wisematches.playground.search;

import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class DesiredEntityDescriptor {
	private DesiredEntityDescriptor() {
	}

	public static SearchAttribute[] getAttributes(Class<? extends DesiredEntity> entity, String... names) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
