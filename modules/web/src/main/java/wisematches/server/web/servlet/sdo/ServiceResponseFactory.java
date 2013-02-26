package wisematches.server.web.servlet.sdo;

import wisematches.playground.GameMessageSource;

import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ServiceResponseFactory {
	private final GameMessageSource messageSource;

	public ServiceResponseFactory(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public ServiceResponse success() {
		return success(null);
	}

	public ServiceResponse success(Map<String, ?> data) {
		return new ServiceResponse(new ServiceResponse.Success(data));
	}

	public ServiceResponse failure(String code, Locale locale) {
		return new ServiceResponse(new ServiceResponse.Failure(code, messageSource.getMessage(code, locale)));
	}

	public ServiceResponse failure(String code, Object[] args, Locale locale) {
		return new ServiceResponse(new ServiceResponse.Failure(code, messageSource.getMessage(code, args, locale)));
	}

}
