package com.yuhtin.quotes.waitlistbot;

import com.yuhtin.quotes.waitlistbot.bot.DiscordBot;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;

import java.util.logging.Logger;

@Getter
public class WaitlistBot implements DiscordBot {

    private static final WaitlistBot INSTANCE = new WaitlistBot();
    private final Logger logger = Logger.getLogger("WaitlistBot");
    private JDA jda;

    @Override
    public void onEnable() {
        getLogger().info("WaitlistBot enabled!");
    }

    @Override
    public void onReady() {
        getLogger().info("Bot ready!");
    }

    @Override
    public void serve(JDA jda) {
        this.jda = jda;
    }

    public static WaitlistBot getInstance() {
        return INSTANCE;
    }

}
