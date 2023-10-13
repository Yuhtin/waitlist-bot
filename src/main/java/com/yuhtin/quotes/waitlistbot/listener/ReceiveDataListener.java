package com.yuhtin.quotes.waitlistbot.listener;

import com.google.gson.Gson;
import com.yuhtin.quotes.waitlistbot.constants.BotConstants;
import com.yuhtin.quotes.waitlistbot.model.webhook.WebhookData;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReceiveDataListener extends ListenerAdapter {

    private static final Gson GSON = new Gson();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannel().getIdLong() != BotConstants.CHANNEL_ID) return;
        if (!event.getMessage().isWebhookMessage()) return;

        String content = event.getMessage().getContentRaw();
        if (!content.startsWith("{")) return;

        WebhookData webhookData = GSON.fromJson(content, WebhookData.class);
        if (webhookData == null) return;

        // TODO
    }
}
