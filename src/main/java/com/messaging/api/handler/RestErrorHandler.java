package com.messaging.api.handler;

import static com.messaging.api.constant.Constants.RESPONSE_FAILED;
import static com.messaging.api.util.JsonUtil.encode;
import static com.messaging.api.util.ResponseUtil.toApiResponse;

import com.messaging.api.exception.ExceptionMapper;
import com.messaging.api.exception.RestException;
import com.messaging.api.model.RestApiError;
import com.messaging.api.model.RestApiResponse;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

public class RestErrorHandler extends BaseHandler {

	public RestErrorHandler(Vertx vertx) {
		super(vertx);
	}

	@Override
	public void handle(RoutingContext routingContext) {
		final RestApiResponse<RestApiError> response;
		final RestException exception = getRestException(routingContext.failure());
		response = toApiResponse(exception.getStatusCode(), RESPONSE_FAILED, ExceptionMapper.build(exception), true);
		routingContext.response().setStatusCode(exception.getStatusCode());
		routingContext.response().end(encode(response));
	}

	private RestException getRestException(Throwable t) {
		if (t instanceof RestException) {
			return (RestException) t;
		} else {
			return new RestException(t.getMessage(), t, 500);
		}
	}

}
