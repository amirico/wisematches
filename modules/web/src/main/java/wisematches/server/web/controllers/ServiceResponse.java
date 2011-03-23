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
    private final Map<String, Object> data;

    public static final ServiceResponse SUCCESS = new ServiceResponse(true, null, null);
    public static final ServiceResponse FAILURE = new ServiceResponse(false, null, null);

    private ServiceResponse(boolean success, String summary, Map<String, Object> data) {
        this.success = success;
        this.summary = summary;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSummary() {
        return summary;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public static ServiceResponse success() {
        return SUCCESS;
    }

    public static ServiceResponse failure() {
        return FAILURE;
    }

    public static ServiceResponse success(String summary) {
        return new ServiceResponse(true, summary, null);
    }

    public static ServiceResponse success(String summary, String dataKey, Object dataValue) {
        return new ServiceResponse(true, summary, Collections.singletonMap(dataKey, dataValue));
    }

    public static ServiceResponse success(String summary, Map<String, Object> data) {
        return new ServiceResponse(true, summary, data);
    }

    public static ServiceResponse failure(String summary) {
        return new ServiceResponse(false, summary, null);
    }

    public static ServiceResponse failure(String summary, String errorKey, Object errorValue) {
        return new ServiceResponse(false, summary, Collections.singletonMap(errorKey, errorValue));
    }

    public static ServiceResponse failure(String summary, Map<String, Object> errors) {
        return new ServiceResponse(false, summary, errors);
    }

    public static ServiceResponse convert(Errors errors) {
        if (errors.hasErrors()) {
            return FAILURE;
//			return new ServiceResponse(false, null, data);
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
