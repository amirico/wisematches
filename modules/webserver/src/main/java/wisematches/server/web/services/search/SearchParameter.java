package wisematches.server.web.services.search;

import java.lang.annotation.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SearchParameter {
	String name();

	boolean sortable() default true;

	String[] sortingColumns() default {};
}
