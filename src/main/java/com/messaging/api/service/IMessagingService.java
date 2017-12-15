package com.messaging.api.service;

import io.reactivex.Single;
import io.vertx.ext.mail.MailResult;

public interface IMessagingService {

	public Single<MailResult> doSend();

}
