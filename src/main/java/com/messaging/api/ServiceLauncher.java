package com.messaging.api;

import com.messaging.api.config.MessagingConfig;
import com.messaging.api.constant.Constants;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.launcher.VertxCommandLauncher;
import io.vertx.core.impl.launcher.VertxLifecycleHooks;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.SLF4JLogDelegateFactory;

public class ServiceLauncher extends VertxCommandLauncher implements VertxLifecycleHooks {

	// make sure you have the logger
	static {
		if (System.getProperty("vertx.logger-delegate-factory-class-name") == null) {
			System.setProperty("vertx.logger-delegate-factory-class-name",
					SLF4JLogDelegateFactory.class.getCanonicalName());
		}
		System.out.println(System.getenv(Constants.ENVIRONMENT));
	}

	@Override
	public void afterConfigParsed(JsonObject config) {
		MessagingConfig.INSTANCE.setConfig(config);
	}

	@Override
	public void beforeStartingVertx(VertxOptions options) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterStartingVertx(Vertx vertx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeStoppingVertx(Vertx vertx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterStoppingVertx() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions,
			Throwable cause) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		new ServiceLauncher().dispatch(args);
	}

	public static void executeCommand(String cmd, String... args) {
		new ServiceLauncher().execute(cmd, args);
	}
}