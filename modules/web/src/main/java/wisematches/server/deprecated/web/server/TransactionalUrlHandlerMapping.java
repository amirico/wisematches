package wisematches.server.deprecated.web.server;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is implementation of {@code BeanNameUrlHandlerMapping} but that checks is bean has any methods
 * with {@code Transactional} annotation or not.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TransactionalUrlHandlerMapping extends AbstractDetectingUrlHandlerMapping {
	private AllowedType allowedType = AllowedType.NONE;

	protected String[] determineUrlsForHandler(String beanName) {
		if (!isBeanSupported(beanName)) {
			return null;
		}

		final Collection<String> urls = new ArrayList<String>();
		if (beanName.startsWith("/")) {
			urls.add(beanName);
		}

		final String[] aliases = getApplicationContext().getAliases(beanName);
		for (String aliase : aliases) {
			if (aliase.startsWith("/")) {
				urls.add(aliase);
			}
		}
		return StringUtils.toStringArray(urls);
	}

	protected boolean isBeanSupported(String beanName) {
		if (allowedType == AllowedType.NONE) {
			return false;
		} else if (allowedType == AllowedType.TRANSACTIONAL_AND_NOT_TRANSACTIONAL) {
			return true;
		}

		// Otherwice check the bean
		final Object bean = getApplicationContext().getBean(beanName);
		if (isTransactionalBean(bean)) {
			return allowedType == AllowedType.TRANSACTIONAL_ONLY;
		}
		return allowedType == AllowedType.NOT_TRANSACTION_ONLY;
	}

	protected boolean isTransactionalBean(Object bean) {
		final Class beanType = bean.getClass();
		if (beanType.isAnnotationPresent(Transactional.class)) {
			return true;
		}
		final Method[] methods = beanType.getMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Transactional.class)) {
				return true;
			}
		}
		return false;
	}

	public AllowedType getAllowedType() {
		return allowedType;
	}

	public void setAllowedType(AllowedType allowedType) {
		if (allowedType == null) {
			throw new NullPointerException("Allowed type can't be null");
		}
		this.allowedType = allowedType;
	}

	public static enum AllowedType {
		NONE,
		TRANSACTIONAL_ONLY,
		NOT_TRANSACTION_ONLY,
		TRANSACTIONAL_AND_NOT_TRANSACTIONAL
	}
}
