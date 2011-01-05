/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WebUtils {
    private WebUtils() {
    }

    public static <T> void setRequestAttribute(HttpServletRequest request, Class<T> type, T attribute) {
        request.setAttribute(type.getSimpleName(), attribute);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getRequestAttrbiute(HttpServletRequest request, Class<T> type) {
        return (T) request.getAttribute(type.getSimpleName());
    }
}
