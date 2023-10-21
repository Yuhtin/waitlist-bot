package com.yuhtin.quotes.waitlistbot;

import com.yuhtin.quotes.waitlistbot.bot.DiscordBot;
import com.yuhtin.quotes.waitlistbot.command.CommandRegistry;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.constants.BotConstants;
import com.yuhtin.quotes.waitlistbot.listener.DataReceiverListener;
import com.yuhtin.quotes.waitlistbot.listener.MessageChatListener;
import com.yuhtin.quotes.waitlistbot.listener.UserJoinGuildListener;
import com.yuhtin.quotes.waitlistbot.manager.UserManager;
import com.yuhtin.quotes.waitlistbot.repository.MongoClientManager;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import com.yuhtin.quotes.waitlistbot.task.RemindTaskRunnable;
import com.yuhtin.quotes.waitlistbot.task.TaskHelper;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
public class WaitlistBot implements DiscordBot {

    private static final WaitlistBot INSTANCE = new WaitlistBot();
    private final Logger logger = Logger.getLogger("WaitlistBot");

    private Config config;
    private Jedis jedis;
    private JDA jda;

    private UserManager userManager;

    @Override
    public void onEnable() {
        formatLogger(logger);
        getLogger().info("Enabling bot...");

        loadConfig();
        setupMongoClient();
        setupRedisClient();

        getLogger().info("Bot enabled!");
    }

    @Override
    public void onReady() {
        getLogger().info("Bot ready!");
        getLogger().info("Logged in as @" + jda.getSelfUser().getName());

        CommandRegistry.of(jda).register();

        this.userManager = new UserManager(jda, config);

        registerPubSub();
        registerRemindTask();
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

    private void setupRedisClient() {
        jedis = new Jedis(new HostAndPort(config.getRedisAddress(), 6379));
        jedis.auth(config.getRedisPassword());

        logger.info("Redis client connected!");
    }

    private void registerPubSub() {
        DataReceiverListener.of(config, userManager).register(jedis);
    }

    private void registerRemindTask() {
        RemindTaskRunnable remindTaskRunnable = new RemindTaskRunnable(this);
        TaskHelper.runTaskTimerAsync(remindTaskRunnable, 10, 10, TimeUnit.MINUTES);
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
