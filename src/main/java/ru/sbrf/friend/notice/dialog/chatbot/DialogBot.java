package ru.sbrf.friend.notice.dialog.chatbot;

import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import im.dlg.botsdk.Bot;
import im.dlg.botsdk.BotConfig;
import im.dlg.botsdk.BotSystem;

import im.dlg.botsdk.model.content.DocumentContent;
import im.dlg.botsdk.model.content.TextContent;

@Component
public class DialogBot {
	static Logger log = LoggerFactory.getLogger(DialogBot.class);

	@Autowired
	BotSystem botSystem;

	@Autowired
	BotConfig botConfig;

	@PostConstruct
	public void startBot() throws InterruptedException, ExecutionException {

		Bot bot = botSystem.startBot(botConfig).get();

		bot.messaging().onMessage(
				message -> bot.users().get(message.getSender()).thenAccept(userOpt -> userOpt.ifPresent(user -> {
					log.debug("Got a message: " + message.getText() + " from user: " + user.getName());
				})).thenCompose(aVoid -> {
					switch (message.getText()) {
					case "Send me photo":
						return bot.messaging().sendMedia(message.getPeer(),
								((TextContent) message.getMessageContent()).getMedias());
					case "Send me video":
						return bot.messaging().sendDocument(message.getPeer(),
								(DocumentContent) message.getMessageContent());
					default:
						return bot.messaging().reply(message.getPeer(), message.getMessageId(), "some reply");
					}
				}).exceptionally(ex -> {
					log.error("", ex);
					return null;
				}).thenAccept(uuid -> log.debug("Sent a message with UUID: {}", uuid)));

		bot.await();
	}

}
