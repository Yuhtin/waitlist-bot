package com.yuhtin.quotes.waitlistbot.model;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import net.dv8tion.jda.api.JDA;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Data
@Builder
@Accessors(fluent = true)
public class User {

    private final String memberId;
    private final String email;
    private final String listId;
    private final String discordName;

    private long discordId;
    private int position;
    private int referrals;

    private int messagesInChat;

    public long retrieveDiscordId() {
        if (discordId == 0) {
            JDA jda = WaitlistBot.getInstance().getJda();

            jda.getUsersByName(discordName, true)
                    .stream()
                    .findFirst()
                    .ifPresent(user -> discordId = user.getIdLong());
        }

        return discordId;
    }

    public String inviteLink() {
        return "https://form.zootools.co/go/" + listId + "?ref=" + memberId;
    }

    public void save() {
        UserRepository.instance().replace(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "memberId='" + memberId + '\'' +
                ", email='" + email + '\'' +
                ", listId='" + listId + '\'' +
                ", discordName='" + discordName + '\'' +
                ", discordId=" + discordId +
                ", position=" + position +
                ", referrals=" + referrals +
                '}';
    }
}
