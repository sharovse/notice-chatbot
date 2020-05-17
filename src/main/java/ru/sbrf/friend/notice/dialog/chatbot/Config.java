package ru.sbrf.friend.notice.dialog.chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import im.dlg.botsdk.BotConfig;
import im.dlg.botsdk.BotSystem;
import im.dlg.botsdk.BotSystemConfig;

@Configuration
public class Config {

	@Value("${bot.token}")
	String botToken;
	
	@Value("${bot.id}")
	String botId;
	
	@Value("${bot.name}")
	String botName;

	@Value("${bot.url}")
	String botUrl;
	
	@Value("${bot.port}")
	int botPort;

	@Value("${bot.cert.pw}")
	private String botCertPw;
	@Value("${bot.cert.pfx.file}")
	private String botCertFile;
	
	@Bean
	BotSystem botSystem() throws Exception {
		BotSystemConfig systemConfig = BotSystemConfig.Builder.newBuilder()
                .withHost(botUrl)
                .withPort(botPort)
                .withCertPath(botCertFile)
                .withCertPassword(botCertPw)                
                .build();
		return BotSystem.createSystem(systemConfig);
	}
	
	@Bean
	BotConfig botConfig() {
		return BotConfig.Builder.newBuilder()
        		.withToken(botToken)
                .withBotName(botName)
        		.build();
		
	}

}
