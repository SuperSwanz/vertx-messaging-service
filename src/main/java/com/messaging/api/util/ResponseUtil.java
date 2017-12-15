package com.messaging.api.util;

import java.util.Date;

import com.messaging.api.constant.Constants;
import com.messaging.api.model.RestApiResponse;

import io.vertx.reactivex.ext.web.RoutingContext;

public final class ResponseUtil {

	public static <T> void toResponse(T result, final RoutingContext routingContext, Date dt) {
		final RestApiResponse<T> response;
		response = toApiResponse(200, Constants.RESPONSE_SUCCESS, (T) result, false);
		final String resp = JsonUtil.encode(response);
		routingContext.response().setStatusCode(response.getCode()).end(resp);
	}

	public static <T> RestApiResponse<T> toApiResponse(final int code, final String message, final T data,
			final boolean hasError) {
		RestApiResponse<T> response = new RestApiResponse.Builder<T>().setCode(code).setHasError(hasError).setData(data)
				.setMessage(message).build();
		return response;
	}

	public static String getHeaderValue(final String headerName, final RoutingContext routingContext) {
		return routingContext.request().getHeader(headerName);
	}

}