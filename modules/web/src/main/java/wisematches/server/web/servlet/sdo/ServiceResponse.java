package wisematches.server.web.servlet.sdo;

import org.springframework.http.HttpEntity;

import java.util.Map;

/**
 * This is service response object that is returned by
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ServiceResponse extends HttpEntity<ServiceResponse.ResponseBody> {
	public ServiceResponse(ResponseBody body) {
		super(body);
	}

	public static class ResponseBody {
		private final boolean success;

		private ResponseBody(boolean success) {
			this.success = success;
		}

		public boolean isSuccess() {
			return success;
		}
	}

	public static final class Failure extends ServiceResponse.ResponseBody {
		private final String code;
		private final String message;

		public Failure(String code, String message) {
			super(false);
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
	}

	public static final class Success extends ServiceResponse.ResponseBody {
		private final Map<String, ?> data;

		public Success(Map<String, ?> data) {
			super(true);
			this.data = data;
		}

		public Map<String, ?> getData() {
			return data;
		}
	}
}