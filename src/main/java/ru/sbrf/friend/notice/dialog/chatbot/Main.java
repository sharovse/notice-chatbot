package ru.sbrf.friend.notice.dialog.chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import ch.qos.logback.core.net.SyslogOutputStream;
import im.dlg.botsdk.Bot;
import im.dlg.botsdk.BotConfig;
import im.dlg.botsdk.BotSystem;
import im.dlg.botsdk.BotSystemConfig;
import im.dlg.botsdk.model.content.DocumentContent;
import im.dlg.botsdk.model.content.TextContent;

public class Main {

	static final String botToken = "89a86b9157394344fbb1500e0985bcd9a70fce38";
	static final String botId = "616907557";
	static final String botName = "sberdrug-test";
	static final String botUrl = "sberchat.sberbank.ru";
	static final int botPort = 443;
	static final String botCertPw = "123sharov";
	static final String botCertFile = "/home/sharov/data/16701861.pfx";

	public static void main(String[] args) throws Exception {
		BotSystemConfig systemConfig = BotSystemConfig.Builder.newBuilder().withHost(botUrl).withPort(botPort)
				.withCertPath(botCertFile).withCertPassword(botCertPw).build();
		BotSystem botSystem = BotSystem.createSystem(systemConfig);

		BotConfig botConfig = BotConfig.Builder.newBuilder().withToken(botToken).withBotName(botName).build();

		Bot bot = botSystem.startBot(botConfig).get();

		bot.messaging().onMessage(
				message -> bot.users().get(message.getSender()).thenAccept(userOpt -> userOpt.ifPresent(user -> {
					System.out.print("Got a message: " + message.getText() + " from user: " + user.getName());
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
					ex.printStackTrace();
					;
					return null;
				}).thenAccept(uuid -> 
					System.out.println("Sent a message with UUID:" + uuid))
				);

		bot.await();

	}

}
