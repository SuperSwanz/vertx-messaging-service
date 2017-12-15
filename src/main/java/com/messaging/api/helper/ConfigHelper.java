package com.messaging.api.helper;

import static com.messaging.api.constant.Constants.ENVIRONMENT;
import static com.messaging.api.constant.Constants.ENVIRONMENT_DEVELOPMENT;
import static com.messaging.api.constant.Constants.REFLECTION_PACKAGE_NAME;
import static com.messaging.api.constant.Constants.VERTICLE_PACKAGE_NAME;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.messaging.api.config.MessagingConfig;
import com.messaging.api.constant.Constants;

import io.vertx.core.json.JsonObject;

public class ConfigHelper {
	private static final JsonObject config = MessagingConfig.INSTANCE.getConfig();

	public static JsonObject loadEnvironment() {
		String environment = System.getenv(ENVIRONMENT);
		environment = !isBlank(environment) ? environment.trim().toLowerCase() : ENVIRONMENT_DEVELOPMENT.toLowerCase();
		return config.getJsonObject(environment);
	}

	public static JsonObject getValueByEnvironment(final String key) {
		return loadEnvironment().getJsonObject(key);
	}

	public static String getVerticlePackage() {
		return config.getString(VERTICLE_PACKAGE_NAME, "com.api.hammer.verticle");
	}

	public static String getReflectionPackage() {
		return config.getString(REFLECTION_PACKAGE_NAME);
	}

	public static String getContextPath() {
		return config.getString(Constants.CONTEXT_PATH);
	}

	public static int getPort() {
		return config.getInteger(Constants.CONFIG_HTTP_SERVER_PORT, 8080);
	}

	public static String getHost() {
		return config.getString(Constants.CONFIG_HTTP_SERVER_HOST, "localhost");
	}

	public static JsonObject getMailconfig() {
		return getValueByEnvironment(Constants.CONFIG_MAIL_NAME);
	}

	public static boolean isHTTP2Enabled() {
		return config.getBoolean(Constants.CONFIG_IS_HTTP2_ENABLED, false);
	}

	public static JsonObject getHTTPServerOptions() {
		return config.getJsonObject(Constants.CONFIG_HTTP_SERVER_OPTIONS, new JsonObject());
	}

	public static boolean isSSLEnabled() {
		return config.getBoolean(Constants.IS_SSL_ENABLED, false);
	}
}