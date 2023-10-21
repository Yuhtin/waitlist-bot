package com.yuhtin.quotes.waitlistbot.command.impl;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.command.Command;
import com.yuhtin.quotes.waitlistbot.command.CommandInfo;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;

@CommandInfo(
        names = {"invite"},
        description = "Shows your invite link"
)
public class InviteCommand implements Command {

    @Override
    public void execute(CommandInteraction command) {
        InteractionHook hook = command.deferReply(true).complete();
        Config config = WaitlistBot.getInstance().getConfig();

        User user = UserRepository.instance().findByDiscordName(command.getUser().getName());
        if (user == null) {
            hook.sendMessage(config.getUserNotFound()).setEphemeral(true).queue();
            return;
        }

        if (user.discordId() == 0) {
            user.discordId(command.getUser().getIdLong());
            user.save();
        }

        hook.sendMessage(config.getInviteMessage().replace("%link%", user.inviteLink()))
                .setEphemeral(true)
                .queue();
    }
}
