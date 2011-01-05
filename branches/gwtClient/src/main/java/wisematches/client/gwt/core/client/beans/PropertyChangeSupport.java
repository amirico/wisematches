package wisematches.client.gwt.core.client.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PropertyChangeSupport<T> implements Serializable {
    private transient T bean;
    private transient Collection<PropertyChangeListener<T>> listeners;

    @SuppressWarnings("unchecked")
    public PropertyChangeSupport() {
        bean = (T) this;
    }

    public PropertyChangeSupport(T bean) {
        this.bean = bean;
    }

    public void addPropertyChangeListener(PropertyChangeListener<T> listener) {
        if (listeners == null) {
            listeners = new HashSet<PropertyChangeListener<T>>();
        }
        listeners.add(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener<T> listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * This method fires event only if at least one listener was added and old value is not equals to new value.
     *
     * @param name     the property name.
     * @param oldValue the old value.
     * @param newValue the new value.
     */
    public void firePropertyChanged(String name, Object oldValue, Object newValue) {
        if (listeners != null && oldValue != newValue && (oldValue == null || !oldValue.equals(newValue))) {
            for (PropertyChangeListener<T> listener : listeners) {
                listener.propertyChanged(bean, name, oldValue, newValue);
            }
        }
    }
}
