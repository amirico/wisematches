package wisematches.playground.search;

import java.lang.annotation.*;

/**
 * {@code SearchAttribute} describes attributes of {@code SearchableEntity} which is provided by search
 * system.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see DesiredEntity
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SearchAttribute {
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
