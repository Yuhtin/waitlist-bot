package com.yuhtin.quotes.waitlistbot.startup;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.bot.BotProperties;
import com.yuhtin.quotes.waitlistbot.bot.DiscordBotConnector;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Arrays;

public class Startup {

    public static void main(String[] args) {
        BotProperties properties = BotProperties.builder()
                .token("MTE2MDMxNjgwOTQwNTI4ODUxOA.GHfBQb._0KEMhAJ_JCeXZ6tkCh8ytyMdMrZvuBReyVmH0")
                .disabledCaches(Arrays.stream(CacheFlag.values()).toList())
                .enabledIntents(Arrays.asList(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT))
                .build();

        DiscordBotConnector
                .connect(WaitlistBot.getInstance())
                .with(properties);
    }

}
