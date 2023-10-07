package com.yuhtin.quotes.waitlistbot.startup;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.bot.DiscordBotConnector;

import java.util.logging.Logger;

public class Startup {

    public static void main(String[] args) {
        DiscordBotConnector connector = DiscordBotConnector.builder()
                .token("")
                .bot(WaitlistBot.getInstance())
                .build();

        connector.init();
    }

}
