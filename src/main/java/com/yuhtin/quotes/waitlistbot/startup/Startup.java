package com.yuhtin.quotes.waitlistbot.startup;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.bot.BotProperties;
import com.yuhtin.quotes.waitlistbot.bot.DiscordBotConnector;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Startup {

    public static void main(String[] args) {
        DiscordBotConnector.connect(WaitlistBot.getInstance());
    }

}
