package com.yuhtin.quotes.waitlistbot.bot;

import com.yuhtin.quotes.waitlistbot.listener.MessageChatListener;
import com.yuhtin.quotes.waitlistbot.listener.UserJoinGuildListener;
import net.dv8tion.jda.api.JDABuilder;

public class DiscordBotConnector {

    public static void connect(DiscordBot bot) {
        bot.onEnable();

        JDABuilder.createDefault(bot.getConfig().getToken())
                .addEventListeners(
                        new BotConnectionListener(bot),
                        new MessageChatListener(bot.getConfig()),
                        new UserJoinGuildListener(bot.getConfig())
                ).build();
    }

}
