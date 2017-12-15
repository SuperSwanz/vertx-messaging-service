package com.messaging.api.exception;

import com.messaging.api.model.RestApiError;

public class ExceptionMapper {

	public static RestApiError build(Exception ex) {
		if (ex instanceof RestException) {
			RestException rx = (RestException) ex;
			final String message = rx.getCause() != null ? rx.getCause().getMessage() : rx.getMessage();
			return new RestApiError.Builder().setCode(rx.getStatusCode()).setMessage(message).build();
		} else {
			return new RestApiError.Builder().setCode(500).setMessage(ex.getMessage()).build();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T build(Throwable cause) {
		return (T) new RestApiError.Builder().setCode(500).setMessage(cause.getMessage()).build();
	}
}