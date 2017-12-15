package com.messaging.api.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import com.messaging.api.constant.MediaType;
import com.messaging.api.exception.RestException;
import com.messaging.api.handler.BaseHandler;
import com.messaging.api.helper.ConfigHelper;

import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.Cookie;
import io.vertx.reactivex.ext.web.Router;

public final class RouteProcessor {
	private static String PACKAGE_NAME = ConfigHelper.getReflectionPackage();
	private static String CONTEXT_PATH = ConfigHelper.getContextPath();

	public static void init(final Router router, final Vertx vertx) {
		final Reflections reflections = new Reflections(PACKAGE_NAME);
		final Set<Class<? extends BaseHandler>> clazzes = reflections.getSubTypesOf(BaseHandler.class);
		if (null != clazzes && !clazzes.isEmpty()) {
			clazzes.forEach(baseHandler -> doScan(router, baseHandler, vertx));
		} else {
			throw new RestException("There are no handlers to configure");
		}
	}

	private static void doScan(final Router router, final Class<? extends BaseHandler> clazz, final Vertx vertx) {
		final Service service = clazz.getAnnotation(Service.class);
		if (null != service) {
			Arrays.stream(clazz.getMethods()).forEach(method -> {
				Get get = method.getAnnotation(Get.class);
				Post post = method.getAnnotation(Post.class);
				Put put = method.getAnnotation(Put.class);
				Delete delete = method.getAnnotation(Delete.class);
				final String url = service.uri();
				if (get != null) {
					registerRoute(router, HttpMethod.GET, url + get.value(), method, clazz, vertx);
				} else if (post != null) {
					registerRoute(router, HttpMethod.POST, url + post.value(), method, clazz, vertx);
				} else if (put != null) {
					registerRoute(router, HttpMethod.PUT, url + put.value(), method, clazz, vertx);
				} else if (delete != null) {
					registerRoute(router, HttpMethod.DELETE, url + delete.value(), method, clazz, vertx);
				}
			});
		}
	}

	private static void registerRoute(final Router router, final HttpMethod method, final String url, final Method m,
			final Class<? extends BaseHandler> clazz, final Vertx vertx) {
		final String _url = StringUtils.join(CONTEXT_PATH, url);
		System.out.println(_url);
		router.route(method, _url).consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
				.handler(ctx -> {
					try {
						// TODO: Need to see a better way of doing this
						Cookie cookie = Cookie.cookie("_currentMethod", m.getDeclaringClass() + "." + m.getName());
						ctx.addCookie(cookie);
						m.invoke(clazz.getDeclaredConstructor(Vertx.class).newInstance(vertx), ctx);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
				});
	}
}