package com.yuhtin.quotes.waitlistbot.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.dv8tion.jda.api.JDABuilder;

@Builder
public class DiscordBotConnector {

    private final String token;
    private final DiscordBot bot;

    public void init() {
        bot.onEnable();

        JDABuilder.createLight(token)
                .addEventListeners(new BotConnectionListener(bot))
                .build();
    }

}
