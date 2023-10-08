package com.yuhtin.quotes.waitlistbot;

import com.yuhtin.quotes.waitlistbot.bot.DiscordBot;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;

import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
public class WaitlistBot implements DiscordBot {

    private static final WaitlistBot INSTANCE = new WaitlistBot();
    private final Logger logger = Logger.getLogger("WaitlistBot");

    private JDA jda;

    @Override
    public void onEnable() {
        formatLogger(logger);

        getLogger().info("Enabling bot...");
        getLogger().info("Bot enabled!");
    }

    @Override
    public void onReady() {
        getLogger().info("Bot ready!");
        getLogger().info("Logged in as @" + jda.getSelfUser().getName());
    }

    @Override
    public void serve(JDA jda) {
        this.jda = jda;
    }

    private void formatLogger(Logger logger) {
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String format = "[%1$tT] [%2$s] %3$s %n";

            @Override
            public synchronized String format(LogRecord record) {
                return String.format(format,
                        new Date(record.getMillis()),
                        record.getLevel().getLocalizedName(),
                        record.getMessage()
                );
            }
        });

        logger.addHandler(handler);
    }

    public static WaitlistBot getInstance() {
        return INSTANCE;
    }

}
