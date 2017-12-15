package com.messaging.api.verticle;

import com.messaging.api.helper.ConfigHelper;

import io.vertx.reactivex.core.AbstractVerticle;

public abstract class BaseVerticle extends AbstractVerticle {
	public static String CONTEXT_PATH = ConfigHelper.getContextPath();
}