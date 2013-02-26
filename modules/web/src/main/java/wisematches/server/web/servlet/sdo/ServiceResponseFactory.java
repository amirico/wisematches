package wisematches.server.web.servlet.sdo;

import wisematches.playground.GameMessageSource;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ServiceResponseFactory {
	private final GameMessageSource messageSource;

	private static final ServiceResponse SUCCESS = new ServiceResponse(new ServiceResponse.Success(null));

	public ServiceResponseFactory(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public ServiceResponse success() {
		return SUCCESS;
	}

	public ServiceResponse success(Object data) {
		return new ServiceResponse(new ServiceResponse.Success(data));
	}

	public ServiceResponse failure(String code, Locale locale) {
		return new ServiceResponse(new ServiceResponse.Failure(code, messageSource.getMessage(code, locale)));
	}

	public ServiceResponse failure(String code, Object[] args, Locale locale) {
		return new ServiceResponse(new ServiceResponse.Failure(code, messageSource.getMessage(code, args, locale)));
	}

}
