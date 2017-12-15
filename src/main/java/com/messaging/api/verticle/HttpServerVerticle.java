package com.messaging.api.verticle;

import java.util.HashSet;
import java.util.Set;

import com.messaging.api.annotation.RouteProcessor;
import com.messaging.api.constant.MediaType;
import com.messaging.api.handler.RestErrorHandler;
import com.messaging.api.helper.ConfigHelper;
import com.messaging.api.helper.HttpClientHelper;
import com.messaging.api.helper.MailClientHelper;

import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.mail.MailConfig;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.mail.MailClient;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.CorsHandler;
import io.vertx.reactivex.ext.web.handler.ResponseContentTypeHandler;

public class HttpServerVerticle extends BaseVerticle {
	protected static Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);
	private Single<HttpServer> server;
	private Router mainRouter;

	@Override
	public void start(Future<Void> startFuture) {
		try {
			super.start();
			this.server = createHttpServer(createOptions(ConfigHelper.isHTTP2Enabled()), buildRouter());
			server.subscribe((result -> {
				HttpClientHelper.INSTANCE.setHttpClient(buildHttpClient());
				MailClientHelper.INSTANCE.setMailClient(buildMailClient());
				startFuture.complete();
				logger.info("HTTP server running on port {}", result.actualPort());
			}), ex -> {
				startFuture.fail(ex);
			});
		} catch (Exception ex) {
			logger.error("Failed to start HTTP Server ", ex);
			startFuture.fail(ex);
		}
	}

	private Single<HttpServer> createHttpServer(final HttpServerOptions httpOptions, final Router router) {
		return vertx.createHttpServer(httpOptions).requestHandler(router::accept).rxListen(ConfigHelper.getPort(),
				ConfigHelper.getHost());
	}

	private HttpServerOptions createOptions(boolean http2) {
		HttpServerOptions serverOptions = new HttpServerOptions(ConfigHelper.getHTTPServerOptions());
		if (http2) {
			serverOptions.setSsl(true)
					.setKeyCertOptions(
							new PemKeyCertOptions().setCertPath("server-cert.pem").setKeyPath("server-key.pem"))
					.setUseAlpn(true);
		}
		return serverOptions;
	}

	private Router buildRouter() {
		this.mainRouter = Router.router(vertx).exceptionHandler((error -> {
			logger.error("Routers not injected", error);
		}));
		mainRouter.route(CONTEXT_PATH + "/*").handler(BodyHandler.create());
		mainRouter.route().handler(ResponseContentTypeHandler.create());
		mainRouter.route(CONTEXT_PATH + "/*").handler(
				CorsHandler.create("*").allowedHeaders(getAllowedHeaders()).exposedHeaders(getAllowedHeaders()));
		mainRouter.route(CONTEXT_PATH + "/*").consumes(MediaType.APPLICATION_JSON_VALUE);
		mainRouter.route(CONTEXT_PATH + "/*").produces(MediaType.APPLICATION_JSON_VALUE);
		RouteProcessor.init(this.mainRouter, vertx);
		mainRouter.route(CONTEXT_PATH + "/*").last().failureHandler(new RestErrorHandler(vertx));
		return this.mainRouter;
	}

	private HttpClient buildHttpClient() {
		HttpClientOptions options = new HttpClientOptions();
		if (ConfigHelper.isSSLEnabled()) {
			options.setSsl(true);
			options.setTrustAll(true);
			options.setVerifyHost(false);
		}
		options.setTryUseCompression(true);
		options.setKeepAlive(true);
		options.setMaxPoolSize(50);
		return vertx.createHttpClient(options);
	}

	private MailClient buildMailClient() {
		MailConfig config = new MailConfig(ConfigHelper.getMailconfig());
		return MailClient.createNonShared(vertx, config);
	}

	private Set<String> getAllowedHeaders() {
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("X-Requested-With");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("Origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("Accept");
		return allowHeaders;
	}
}