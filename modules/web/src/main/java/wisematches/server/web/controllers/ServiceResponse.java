/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.controllers;

import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ServiceResponse {
	private final boolean success;
	private final String summary;
	private final Map<String, String> errors;

	public static final ServiceResponse SUCCESS = new ServiceResponse(true, null, null);
	public static final ServiceResponse FAILURE = new ServiceResponse(false, null, null);

	private ServiceResponse(boolean success, String summary, Map<String, String> errors) {
		this.success = success;
		this.summary = summary;
		this.errors = errors;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getSummary() {
		return summary;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public static ServiceResponse success() {
		return SUCCESS;
	}

	public static ServiceResponse failure() {
		return FAILURE;
	}

	public static ServiceResponse failure(String summary) {
		return new ServiceResponse(false, summary, null);
	}

	public static ServiceResponse failure(String summary, String errorKey, String errorValue) {
		return new ServiceResponse(false, summary, Collections.singletonMap(errorKey, errorValue));
	}

	public static ServiceResponse failure(String summary, Map<String, String> errors) {
		return new ServiceResponse(false, summary, errors);
	}

	public static ServiceResponse convert(Errors errors) {
		if (errors.hasErrors()) {
			return FAILURE;
//			return new ServiceResponse(false, null, errors);
		}
		return SUCCESS;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ServiceResponse");
		sb.append("{success=").append(success);
		sb.append(", summary='").append(summary).append('\'');
		sb.append(", errors=").append(errors);
		sb.append('}');
		return sb.toString();
	}
}
