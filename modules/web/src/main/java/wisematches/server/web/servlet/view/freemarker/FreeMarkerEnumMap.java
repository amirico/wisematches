package wisematches.server.web.servlet.view.freemarker;

import org.springframework.ui.Model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class FreeMarkerEnumMap<E extends Enum<E>> implements Map<String, E> {
	private final Map<String, E> internalMap;

	private static final Map<Class<?>, FreeMarkerEnumMap<?>> cache = new HashMap<>();

	private FreeMarkerEnumMap(Class<E> type) {
		final E[] enumConstants = type.getEnumConstants();
		internalMap = new HashMap<>(enumConstants.length);
		for (E enumConstant : enumConstants) {
			internalMap.put(enumConstant.name(), enumConstant);
		}
	}

	@Override
	public int size() {
		return internalMap.size();
	}

	@Override
	public boolean isEmpty() {
		return internalMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return internalMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return internalMap.containsValue(value);
	}

	@Override
	public E put(String key, E value) {
		throw new UnsupportedOperationException("Not changeable");
	}

	@Override
	public void putAll(Map<? extends String, ? extends E> m) {
		throw new UnsupportedOperationException("Not changeable");
	}

	@Override
	public E get(Object key) {
		return internalMap.get(key);
	}

	@Override
	public E remove(Object key) {
		throw new UnsupportedOperationException("Not changeable");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Not changeable");
	}

	@Override
	public Set<String> keySet() {
		return internalMap.keySet();
	}

	@Override
	public Collection<E> values() {
		return internalMap.values();
	}

	@Override
	public Set<Entry<String, E>> entrySet() {
		return internalMap.entrySet();
	}

	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> FreeMarkerEnumMap<E> valueOf(Class<E> type) {
		FreeMarkerEnumMap<E> enumView = (FreeMarkerEnumMap<E>) cache.get(type);
		if (enumView == null) {
			enumView = new FreeMarkerEnumMap<>(type);
			cache.put(type, enumView);
		}
		return enumView;
	}

	public static <E extends Enum<E>> void expose(Class<E> type, Model model) {
		final FreeMarkerEnumMap<E> view = FreeMarkerEnumMap.valueOf(type);
		model.addAllAttributes(view.internalMap);
	}
}
