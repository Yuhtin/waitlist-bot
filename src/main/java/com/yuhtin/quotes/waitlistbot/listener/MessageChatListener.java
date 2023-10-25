package com.yuhtin.quotes.waitlistbot.listener;

import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
public class MessageChatListener extends ListenerAdapter {

    private final Config config;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getChannel().getIdLong() != config.getWaitlistChatChannelId()) return;

        User user = UserRepository.instance().findByDiscordName(event.getAuthor().getName());
        if (user == null) return;

        if (user.discordId() == 0) {
            user.discordId(event.getAuthor().getIdLong());
        }

        user.messagesInChat(user.messagesInChat() + 1);
        user.save();

        if (user.messagesInChat() == 4 || user.messagesInChat() == 20) {
            user.givePoints(10);
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F389")).queue();
        }
    }

}
