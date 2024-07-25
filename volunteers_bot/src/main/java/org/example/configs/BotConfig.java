package org.example.configs;

import lombok.extern.slf4j.Slf4j;
import org.example.bots.LongPoolingBot;
import org.example.services.CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class BotConfig {
    @Value("${telegram.bot.enabled}")
    private boolean isBotEnabled;
    @Value("${telegram.bot.name}")
    private String name;
    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public LongPoolingBot longPoolingBot() {
        if (isBotEnabled) {
            LongPoolingBot longPoolingBot = new LongPoolingBot(name, token);
            log.info("Bot initialized");
            return longPoolingBot;
        }

        log.error("Bot was not initialized");
        return null;
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(LongPoolingBot myTelegramLongPoolingBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(myTelegramLongPoolingBot);
        return botsApi;
    }

    @Bean
    public CommandRegistry commandRegistry(@Autowired CommandService commandService) {
        return commandService.registerCommands(name);
    }
}
