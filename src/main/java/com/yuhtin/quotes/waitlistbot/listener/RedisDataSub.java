package com.yuhtin.quotes.waitlistbot.listener;

import com.google.gson.Gson;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.model.WebhookData;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.logging.Logger;

@AllArgsConstructor(staticName = "of")
public class RedisDataSub extends JedisPubSub {

    private static final String CHANNEL = "waitlistbot_pubsub";
    private static final Logger LOGGER = Logger.getLogger("WaitlistBot");
    private static final Gson GSON = new Gson();

    private final Config config;
    private final JDA jda;

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

        LOGGER.info("Received new data: " + message);

        WebhookData data = GSON.fromJson(message, WebhookData.class);
        if (data == null) return;

        User user = User.builder()
                .memberId(data.getId())
                .email(data.getEmail())
                .creationDate(data.getSubscriptionDate())
                .discordName(data.getDiscord())
                .position(data.getPosition())
                .build();

        UserRepository.instance().insert(user);
        LOGGER.info("User " + user.email() + " updated in database!");

        updateSubscribersChannelCount(data.getSubscribersCount());
    }

    private void updateSubscribersChannelCount(int subscribersCount) {
        VoiceChannel channel = jda.getVoiceChannelById(config.getSubscribersCountChannelId());
        if (channel == null) return;

        channel.getManager()
                .setName("Waitlist Subscribers: " + subscribersCount)
                .queue(t -> LOGGER.info("Updated subscribers count to " + subscribersCount));
    }
}
