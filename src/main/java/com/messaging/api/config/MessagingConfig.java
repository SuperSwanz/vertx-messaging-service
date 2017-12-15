package com.messaging.api.config;

import io.vertx.core.json.JsonObject;

public enum MessagingConfig {
	INSTANCE;
	private JsonObject config;

	public JsonObject getConfig() {
		return config;
	}

	public void setConfig(final JsonObject config) {
		this.config = config;
	}
}
