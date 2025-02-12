package com.yuhtin.quotes.waitlistbot.bot;

import com.yuhtin.quotes.waitlistbot.task.TaskHelper;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class BotConnectionListener extends ListenerAdapter {

    private final DiscordBot bot;


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        bot.serve(event.getJDA());
        TaskHelper.runAsync(bot::onReady);
    }
}