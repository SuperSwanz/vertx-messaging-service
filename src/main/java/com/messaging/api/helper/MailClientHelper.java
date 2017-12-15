package com.messaging.api.helper;

import io.vertx.reactivex.ext.mail.MailClient;

public enum MailClientHelper {
	INSTANCE;
	private MailClient mailClient;

	public MailClient getMailClient() {
		return this.mailClient;
	}

	public void setMailClient(MailClient mailClient) {
		this.mailClient = mailClient;
	}

}
