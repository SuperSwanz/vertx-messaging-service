package com.messaging.api.verticle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.messaging.api.helper.ConfigHelper;

import io.vertx.core.Context;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.CompositeFuture;

public class MainVerticle extends AbstractVerticle {
	@SuppressWarnings("rawtypes")
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		final Reflections reflections = new Reflections(ConfigHelper.getVerticlePackage());
		final Set<Class<? extends BaseVerticle>> clazzes = reflections.getSubTypesOf(BaseVerticle.class);
		List<io.vertx.reactivex.core.Future> futures = new ArrayList<io.vertx.reactivex.core.Future>();
		if (null != clazzes && !clazzes.isEmpty()) {
			for (Class<? extends BaseVerticle> verticle : clazzes) {
				final String clazzFullName = verticle.getCanonicalName();
				final String clazzName = verticle.getSimpleName();
				futures.add(deploy(clazzFullName, clazzName));
			}

			CompositeFuture.all(futures).setHandler(ar -> {
				if (ar.succeeded()) {
					System.out.println("All verticles deployed successfully");
					startFuture.complete();
				} else {
					ar.cause().printStackTrace();
					System.out.println("An error occured while deploying verticles");
					startFuture.fail(ar.cause());
				}

			});
		} else {
			startFuture.fail("No verticles to deploy");
		}
	}

	@Override
	public void init(Vertx vertx, Context context) {
		super.init(vertx, context);
	}

	private io.vertx.reactivex.core.Future<Void> deploy(final String clazzFullName, final String clazzName) {
		io.vertx.reactivex.core.Future<Void> future = io.vertx.reactivex.core.Future.future();
		vertx.deployVerticle(clazzFullName, getDeploymentOptions(clazzName), ar -> {
			if (ar.succeeded()) {
				System.out.println(clazzName + " deployed successfully with deployment id: " + ar.result());
				future.complete();
			} else {
				System.out.println("Failed to deploy verticle " + clazzName);
				future.fail(ar.cause());
			}
		});
		return future;
	}

	private DeploymentOptions getDeploymentOptions(final String clazzName) {
		final String key = clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1);
		final JsonObject json = ConfigHelper.getValueByEnvironment(key);
		DeploymentOptions options = new DeploymentOptions(json);
		return options;
	}
}
