package com.yuhtin.quotes.waitlistbot.listener;

import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.model.RemindUser;
import com.yuhtin.quotes.waitlistbot.repository.RemindRepository;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */

@AllArgsConstructor
public class UserJoinGuildListener extends ListenerAdapter {

    private final Config config;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        User user = event.getMember().getUser();
        user.openPrivateChannel().queue(channel -> channel.sendMessage(config.getReadMeMessage()).queue());

        RemindRepository.instance().insert(new RemindUser(
                user.getIdLong(),
                System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)
        ));

        Logger.getLogger("WaitlistBot").info("→ User " + user.getName() + " joined the guild!");

    }
}
