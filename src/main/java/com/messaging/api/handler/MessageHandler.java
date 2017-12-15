package com.messaging.api.handler;

import static com.messaging.api.util.ResponseUtil.toResponse;

import java.util.Date;

import com.messaging.api.annotation.Post;
import com.messaging.api.annotation.Service;
import com.messaging.api.model.Message;
import com.messaging.api.model.MessageConverter;
import com.messaging.api.service.IMessagingService;
import com.messaging.api.service.VertxMessagingServiceImpl;
import com.messaging.api.util.JsonUtil;

import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

@Service(uri = "/message")
public class MessageHandler extends BaseHandler {
	private IMessagingService messagingService;

	public MessageHandler(Vertx vertx) {
		super(vertx);
	}

	@Override
	@Post(value = "/send")
	public void handle(RoutingContext event) {
		try {
			Message message = JsonUtil.decode(event.getBodyAsString(), Message.class);
			messagingService = new VertxMessagingServiceImpl(MessageConverter.toMailMessage(message, event.vertx()));
			messagingService.doSend().subscribe(response -> {
				toResponse(response, event, new Date());
			}, ex -> {
				event.fail(ex);
			});
		} catch (Exception ex) {
			event.fail(ex);
		}
	}
}