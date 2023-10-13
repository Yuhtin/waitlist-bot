package com.yuhtin.quotes.waitlistbot.listener;

import com.google.gson.Gson;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.model.DataActionType;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.model.WebhookData;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@AllArgsConstructor
public class ReceiveDataListener extends ListenerAdapter {

    private static final Gson GSON = new Gson();
    private static final Logger logger = Logger.getLogger("WaitlistBot");

    private final Config config;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannel().getIdLong() != config.getWebhookChannelId()) return;
        if (!event.getMessage().isWebhookMessage()) return;

        String content = event.getMessage().getContentRaw();
        if (!content.startsWith("{")) return;

        WebhookData webhookData = GSON.fromJson(content, WebhookData.class);
        if (webhookData == null) return;

        User user = User.builder()
                .creationDate(webhookData.getCreationDate())
                .email(webhookData.getMemberEmail())
                .discordName(null)
                .build();

        if (webhookData.getActionType() == DataActionType.ADDED) {
            UserRepository.instance().insert(user);
            logger.info("Added user " + user.email() + " to the database");
        } else if (webhookData.getActionType() == DataActionType.UPDATED) {
            UserRepository.instance().replace(user);
            logger.info("Updated user " + user.email() + " in the database");
        }

        updateSubscribersChannelCount(event.getGuild(), webhookData.getSubscribersCount());
    }

    private void updateSubscribersChannelCount(Guild guild, int subscribersCount) {
        VoiceChannel channel = guild.getVoiceChannelById(config.getSubscribersCountChannelId());
        if (channel == null) return;

        channel.getManager()
                .setName("Waitlist Subscribers: " + subscribersCount)
                .queue();

        logger.info("Updated subscribers count to " + subscribersCount);
    }
}
