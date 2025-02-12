package com.yuhtin.quotes.waitlistbot.manager;

import com.google.gson.Gson;
import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.constants.BotConstants;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import com.yuhtin.quotes.waitlistbot.util.HTTPRequest;
import com.yuhtin.quotes.waitlistbot.util.Promise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    public void updateMemberPosition(String memberId) {
        Promise.supply(() -> HTTPRequest.to(
                BotConstants.ZOOTOOLS_GET_USER_ENDPOINT
                        .replace("{listId}", WaitlistBot.getInstance().getConfig().getZootoolsListId())
                        .replace("{memberId}", memberId),
                WaitlistBot.getInstance().getConfig().getZootoolsApiKey()
        ).send()).then(response -> {
            if (response == null) return;

            User user = UserRepository.instance().findByMemberId(memberId);
            if (user == null) return;

            MemberResponse memberData = new Gson().fromJson(response.getResponse(), MemberResponse.class);
            if (memberData == null) return;

            if (user.position() == memberData.getRankingPosition()) return;

            user.position(memberData.getRankingPosition());
            user.save();

            LOGGER.info("→ Updated " + user.email() + " position to " + user.position());

            jda.retrieveUserById(user.retrieveDiscordId()).queue(discordUser -> {
                if (discordUser == null || !discordUser.hasPrivateChannel()) return;

                discordUser.openPrivateChannel().queue(channel -> channel.sendMessage(config.getPositionMessage()
                        .replace("%user%", discordUser.getAsMention())
                        .replace("%position%", String.valueOf(user.position()))
                ).queue());

                LOGGER.info("→ Sent a message to " + user.email() + " with the new position");
            });
        });
    }

    public void announceReferralUse(User newUser, String referralUserId) {
        if (referralUserId == null || referralUserId.isEmpty() || referralUserId.equalsIgnoreCase("none")) return;
        if (newUser.memberId().equals(referralUserId)) return;

        User user = UserRepository.instance().findByMemberId(referralUserId);
        if (user == null) return;

        user.referrals(user.referrals() + 1);
        user.save();

        if (user.discordName() == null) return;

        if (newUser.discordName() != null) {
            sendReferralUsedMessage(newUser.discordName(), user);
        }

        if (user.referrals() != 2) return;

        JDA jda = WaitlistBot.getInstance().getJda();
        Guild guildById = jda.getGuildById(WaitlistBot.getInstance().getConfig().getGuildId());
        if (guildById == null) {
            throw new IllegalStateException("Guild not found!");
        }

        giveAccessRole(guildById, user);
        sendAchievedAccessRoleMessage(jda, user);
    }

    private void sendReferralUsedMessage(String newUser, User user) {
        if (user.discordName() == null) return;

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
        if (user.discordName() == null) return;

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

        long id = user.retrieveDiscordId();
        if (id == 0) return;

        guildById.addRoleToMember(UserSnowflake.fromId(id), role).queue();
    }

    @Getter
    @RequiredArgsConstructor
    private static class MemberResponse {

        private final int rankingPosition;

    }

}
