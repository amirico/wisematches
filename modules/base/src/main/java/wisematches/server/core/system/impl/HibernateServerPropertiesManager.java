package wisematches.server.core.system.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.core.system.ServerPropertiesManager;
import wisematches.server.core.system.ServerPropertyType;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HibernateServerPropertiesManager extends HibernateDaoSupport implements ServerPropertiesManager {
    private final Map<String, ServerPropertyValue> map = new HashMap<String, ServerPropertyValue>();

    private static final Log log = LogFactory.getLog(HibernateServerPropertiesManager.class);

    private final Lock lock = new ReentrantLock();

    public HibernateServerPropertiesManager() {
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    protected void initDao() throws Exception {
        final HibernateTemplate template = getHibernateTemplate();

        @SuppressWarnings("unchecked")
        final List<ServerPropertyValue> list = template.find("from " + ServerPropertyValue.class.getName());
        for (ServerPropertyValue property : list) {
            map.put(property.getName(), property);
        }

        if (log.isDebugEnabled()) {
            log.debug("ServerProperties loaded: " + map.keySet());
        }
    }

    public Collection<String> getServerPropertyTypes() {
        return Collections.unmodifiableCollection(map.keySet());
    }

    public <T extends Serializable> T getPropertyValue(Class<? extends ServerPropertyType<T>> type, T defaultValue) {
        lock.lock();
        try {
            final ServerPropertyValue value = map.get(type.getName());
            if (value == null) {
                return defaultValue;
            }
            @SuppressWarnings("unchecked")
            final T res = (T) value.getValue();
            {
                //This is hack for Idea to separate 'res'
            }
            return res;
        } finally {
            lock.unlock();
        }
    }

    public <T extends Serializable> T getPropertyValue(Class<? extends ServerPropertyType<T>> type) {
        return getPropertyValue(type, null);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public <T extends Serializable> void setPropertyValue(Class<? extends ServerPropertyType<T>> type, T value) {
        lock.lock();
        try {
            final HibernateTemplate template = getHibernateTemplate();
            final String name = type.getName();
            ServerPropertyValue spv = map.get(name);
            if (spv == null) {
                spv = new ServerPropertyValue(name, value);
                map.put(name, spv);
                template.save(spv);
            } else {
                spv.setValue(value);
                template.update(spv);
            }
            template.flush();
        } finally {
            lock.unlock();
        }
    }
}
