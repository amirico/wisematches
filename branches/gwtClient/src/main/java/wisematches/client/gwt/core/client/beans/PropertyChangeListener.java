package wisematches.client.gwt.core.client.beans;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PropertyChangeListener<T> {
    void propertyChanged(T bean, String property, Object oldValue, Object newValue);
}
