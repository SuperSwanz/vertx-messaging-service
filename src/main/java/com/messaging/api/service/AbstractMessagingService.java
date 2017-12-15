package com.messaging.api.service;

import com.messaging.api.helper.MailClientHelper;

import io.vertx.ext.mail.MailMessage;
import io.vertx.reactivex.ext.mail.MailClient;

public abstract class AbstractMessagingService implements IMessagingService {
	protected MailMessage mailMessage;
	protected MailClient mailClient = MailClientHelper.INSTANCE.getMailClient();

	public AbstractMessagingService(final MailMessage mailMessage) {
		this.mailMessage = mailMessage;
	}
}