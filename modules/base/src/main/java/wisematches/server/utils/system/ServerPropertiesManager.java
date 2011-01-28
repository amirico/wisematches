package wisematches.server.utils.system;

import java.io.Serializable;
import java.util.Collection;

/**
 * Manager of server properties that is stored in some storage, like database.
 * <p/>
 * Each property must have a type: interface that implements {@code ServerPropertyType} interface. This
 * approach allows validate types at compile time.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ServerPropertiesManager {
	/**
	 * Returns unmidifiable collection of all known property types.
	 * <p/>
	 * Returned collection contains class names of all types. We are using String because we can't guarantee
	 * that class still exist and was not removed.
	 *
	 * @return the collection of all known property types.
	 */
	Collection<String> getServerPropertyTypes();

	/**
	 * Returns value of property by it's type.
	 *
	 * @param type the type of property that value should be returned.
	 * @param <T>  the java type of value.
	 * @return the value of property or {@code null} if value is unknown.
	 * @throws ClassCastException if type of property value is illegal.
	 */
	<T extends Serializable> T getPropertyValue(Class<? extends ServerPropertyType<T>> type);

	/**
	 * Returns value of property by it's type.
	 *
	 * @param type		 the type of property that value should be returned.
	 * @param <T>          the java type of value.
	 * @param defaultValue the default value that will be returned if property is not found.
	 * @return the value of property or {@code defaultValue} if value is unknown.
	 * @throws ClassCastException if type of property value is illegal.
	 */
	<T extends Serializable> T getPropertyValue(Class<? extends ServerPropertyType<T>> type, T defaultValue);

	/**
	 * Changes current value of property.
	 *
	 * @param type  the type of property that value should be returned.
	 * @param value new property value.
	 * @param <T>   the java type of value.
	 * @throws ClassCastException if type of property value is illegal.
	 */
	<T extends Serializable> void setPropertyValue(Class<? extends ServerPropertyType<T>> type, T value);
}