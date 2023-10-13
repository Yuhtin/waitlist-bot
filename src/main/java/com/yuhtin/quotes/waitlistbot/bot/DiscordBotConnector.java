package com.yuhtin.quotes.waitlistbot.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor(staticName = "connect")
public class DiscordBotConnector {

    private final DiscordBot bot;

    public void with(BotProperties properties) {
        bot.onEnable();

        JDABuilder.createLight(bot.getConfig().getToken())
                .addEventListeners(new BotConnectionListener(bot))
                .disableCache(properties.getDisabledCaches())
                .setDisabledIntents(Arrays.stream(GatewayIntent.values()).collect(Collectors.toList()))
                .setEnabledIntents(properties.getEnabledIntents())
                .build();
    }

}
