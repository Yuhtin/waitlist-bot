package com.yuhtin.quotes.waitlistbot.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.manager.UserManager;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.model.RedisData;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import com.yuhtin.quotes.waitlistbot.repository.mongo.OperationType;
import com.yuhtin.quotes.waitlistbot.task.TaskHelper;
import lombok.AllArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

@AllArgsConstructor(staticName = "of")
public class DataReceiverListener extends JedisPubSub {


    private static final String CHANNEL = "waitlistbot_pubsub";
    private static final Logger LOGGER = Logger.getLogger("WaitlistBot");
    private static final Gson GSON = new Gson();

    private final Config config;
    private final UserManager manager;

    private final Cache<String, Object> timeout = CacheBuilder.newBuilder()
            .expireAfterWrite(Duration.of(5, ChronoUnit.SECONDS))
            .build();

    public void register(Jedis jedis) {
        jedis.subscribe(this, CHANNEL);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        if (channel.equalsIgnoreCase(CHANNEL)) {
            LOGGER.info("Linked & Subscribed successfully to Redis!");
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equalsIgnoreCase(CHANNEL)) return;

        RedisData data = GSON.fromJson(message, RedisData.class);
        if (data == null) return;

        if (!config.getZootoolsListId().equals(data.getListId())) return;

        String discordName = data.getDiscord().contains("#")
                ? null
                : data.getDiscord().toLowerCase();

        User user = User.builder()
                .memberId(data.getId())
                .email(data.getEmail())
                .discordName(discordName)
                .position(data.getPosition())
                .build();

        // avoid zootools webhook spam
        if (timeout.getIfPresent(user.memberId()) != null) return;
        timeout.put(user.memberId(), new Object());

        // avoid redis listener shutdown
        TaskHelper.runAsync(() -> {
            OperationType operation = UserRepository.instance().insert(user);
            if (operation == OperationType.INSERT) {
                manager.announceReferralUse(user, data.getReferralUserId());
                LOGGER.info("User " + user.email() + " added in database!");
            }

            if (data.getSubscribersCount() != -1) {
                manager.updateSubscribersChannelCount(data.getSubscribersCount());
            }
        });
    }

}
