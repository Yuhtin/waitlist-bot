package com.yuhtin.quotes.waitlistbot.manager;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.logging.Logger;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@AllArgsConstructor
public class UserManager {

    private static final Logger LOGGER = Logger.getLogger("WaitlistBot");

    private final JDA jda;
    private final Config config;

    public void announceReferralUse(String newUser, String referralUserId) {
        if (referralUserId == null) {
            LOGGER.info("User joined without referral!");
            return;
        }

        User user = UserRepository.instance().find(referralUserId);
        if (user == null) return;

        LOGGER.info("User joined with referral of " + user.discordName());

        user.referrals(user.referrals() + 1);
        user.save();

        sendReferralUsedMessage(newUser, user);

        if (user.referrals() > 2) return;

        JDA jda = WaitlistBot.getInstance().getJda();
        Guild guildById = jda.getGuildById(WaitlistBot.getInstance().getConfig().getGuildId());
        if (guildById == null) {
            throw new IllegalStateException("Guild not found!");
        }

        giveAccessRole(guildById, user);
        sendAchievedAccessRoleMessage(jda, user);
    }

    private void sendReferralUsedMessage(String newUser, User user) {
        TextChannel channel = jda.getTextChannelById(config.getWaitlistChatChannelId());
        if (channel == null) {
            throw new IllegalStateException("Waitlist chat channel not found!");
        }

        channel.sendMessage(config.getJoinedWithReferralMessage()
                        .replace("%user%", newUser)
                        .replace("%referralOwner%", user.discordId() == 0 ? user.discordName() : "<@" + user.discordId() + ">"))
                .queue();
    }

    public void updateSubscribersChannelCount(int subscribersCount) {
        VoiceChannel channel = jda.getVoiceChannelById(config.getSubscribersCountChannelId());
        if (channel == null) return;

        channel.getManager()
                .setName("Waitlist Subscribers: " + subscribersCount)
                .queue();
    }

    private void sendAchievedAccessRoleMessage(JDA jda, User user) {
        TextChannel channel = jda.getTextChannelById(config.getWaitlistChatChannelId());
        if (channel == null) {
            throw new IllegalStateException("Waitlist chat channel not found!");
        }

        channel.sendMessage(config.getAchievedAccessRoleMessage().replace("%user%", user.discordName()))
                .queue();
    }

    private void giveAccessRole(Guild guildById, User user) {
        Role role = guildById.getRoleById(config.getAccessRoleId());
        if (role == null) {
            throw new IllegalStateException("Access role not found!");
        }

        guildById.addRoleToMember(UserSnowflake.fromId(user.retrieveDiscordId()), role).queue();
    }

}
