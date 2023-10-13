package com.yuhtin.quotes.waitlistbot;

import com.yuhtin.quotes.waitlistbot.bot.DiscordBot;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.constants.BotConstants;
import com.yuhtin.quotes.waitlistbot.repository.MongoClientManager;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
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

    @Getter private Config config;
    private JDA jda;

    @Override
    public void onEnable() {
        formatLogger(logger);
        getLogger().info("Enabling bot...");

        loadConfig();
        setupMongoClient();

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

    private void loadConfig() {
        config = Config.loadConfig("config.yml");
        if (config == null) {
            System.exit(0);
            return;
        }

        logger.info("Config loaded!");
    }

    private void setupMongoClient() {
        String mongoUri = BotConstants.MONGO_URI
                .replace("$login$", config.getMongoLogin())
                .replace("$address$", config.getMongoAddress());

        MongoClientManager mongoClientManager = MongoClientManager.getInstance();
        mongoClientManager.load(mongoUri, config.getMongoDatabase());

        mongoClientManager.injectTables(UserRepository.instance());
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
