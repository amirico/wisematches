package wisematches.playground.search.descriptive;

import java.lang.annotation.*;

/**
 * {@code SearchAttribute} describes attributes of {@code SearchableEntity} which is provided by search
 * system.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see wisematches.playground.search.DesiredEntityBean
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SearchableProperty {
	/**
	 * Returns appropriate column for the attribute.
	 *
	 * @return the appropriate column name.
	 */
	String column();

	/**
	 * Indicates is attribute is sortable or not.
	 *
	 * @return {@code true} attribute is sortable; {@code false} - otherwise.
	 */
	boolean sortable() default true;
}
