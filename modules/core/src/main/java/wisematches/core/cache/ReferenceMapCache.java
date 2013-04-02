package wisematches.core.cache;

import org.springframework.cache.Cache;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReferenceMapCache implements Cache {
	private final String name;
	private final ReferenceType referenceType;

	private final Map<Object, ReferenceType.CacheValue> cache = new HashMap<>();
	private final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();

	public ReferenceMapCache(String name, ReferenceType referenceType) {
		this.name = name;
		this.referenceType = referenceType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getNativeCache() {
		return cache;
	}

	@Override
	public ValueWrapper get(Object key) {
		clearDeadReferences();
		return cache.get(key);
	}

	@Override
	public void put(Object key, Object value) {
		clearDeadReferences();
		cache.put(key, referenceType.createCacheReference(key, value, referenceQueue));
	}

	@Override
	public void evict(Object key) {
		clearDeadReferences();
		cache.remove(key);
	}

	@Override
	public void clear() {
		cache.clear();
	}

	private void clearDeadReferences() {
		ReferenceType.CacheValue reference = (ReferenceType.CacheValue) referenceQueue.poll();
		while (reference != null) {
			cache.remove(reference.getKey());
			reference = (ReferenceType.CacheValue) referenceQueue.poll();
		}
	}
}
