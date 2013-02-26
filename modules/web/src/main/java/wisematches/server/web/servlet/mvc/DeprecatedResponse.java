/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.servlet.mvc;

import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public final class DeprecatedResponse {
	private final boolean success;
	private final String summary;
	private final Map<String, Object> data;

	public static final DeprecatedResponse SUCCESS = new DeprecatedResponse(true, null, null);
	public static final DeprecatedResponse FAILURE = new DeprecatedResponse(false, null, null);

	private DeprecatedResponse(boolean success, String summary, Map<String, Object> data) {
		this.success = success;
		this.summary = summary;
		this.data = data;
	}


	public String getError() {
		if (success) {
			return null;
		}
		return summary;
	}

	public String getSummary() {
		return summary;
	}

	public boolean isSuccess() {
		return success;
	}

	public Map<String, Object> getData() {
		return data;
	}

	@Deprecated
	public static DeprecatedResponse success() {
		return SUCCESS;
	}

	@Deprecated
	public static DeprecatedResponse failure() {
		return FAILURE;
	}

	@Deprecated
	public static DeprecatedResponse success(String summary) {
		return new DeprecatedResponse(true, summary, null);
	}

	@Deprecated
	public static DeprecatedResponse success(String summary, String dataKey, Object dataValue) {
		return new DeprecatedResponse(true, summary, Collections.singletonMap(dataKey, dataValue));
	}

	@Deprecated
	public static DeprecatedResponse success(String summary, Map<String, Object> data) {
		return new DeprecatedResponse(true, summary, data);
	}

	@Deprecated
	public static DeprecatedResponse failure(String summary) {
		return new DeprecatedResponse(false, summary, null);
	}

	@Deprecated
	public static DeprecatedResponse failure(String summary, String errorKey, Object errorValue) {
		return new DeprecatedResponse(false, summary, Collections.singletonMap(errorKey, errorValue));
	}

	@Deprecated
	public static DeprecatedResponse failure(String summary, Map<String, Object> errors) {
		return new DeprecatedResponse(false, summary, errors);
	}

	@Deprecated
	public static DeprecatedResponse convert(Errors errors) {
		if (errors.hasErrors()) {
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ServiceResponse");
		sb.append("{success=").append(success);
		sb.append(", summary='").append(summary).append('\'');
		sb.append(", errors=").append(data);
		sb.append('}');
		return sb.toString();
	}
}
