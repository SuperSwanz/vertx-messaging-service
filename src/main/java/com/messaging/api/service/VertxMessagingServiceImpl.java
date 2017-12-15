package com.messaging.api.service;

import io.reactivex.Single;
import io.vertx.ext.mail.MailMessage;
import io.vertx.ext.mail.MailResult;

public class VertxMessagingServiceImpl extends AbstractMessagingService {

	public VertxMessagingServiceImpl(final MailMessage mailMessage) {
		super(mailMessage);
	}

	public Single<MailResult> doSend() {
		return this.mailClient.rxSendMail(mailMessage);
	}
}