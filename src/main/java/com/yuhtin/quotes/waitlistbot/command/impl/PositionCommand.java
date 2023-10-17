package com.yuhtin.quotes.waitlistbot.command.impl;

import com.yuhtin.quotes.waitlistbot.command.Command;
import com.yuhtin.quotes.waitlistbot.command.CommandInfo;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;

@CommandInfo(name = "position", description = "Shows your position in the waitlist")
public class PositionCommand implements Command {

    @Override
    public void execute(CommandInteraction command, InteractionHook hook) {
        User user = UserRepository.instance().findByDiscordName(command.getUser().getName());
        if (user == null) {
            hook.sendMessage("📌 You are not in the waitlist!\n🔹 Join us in https://soba.xyz").setEphemeral(true).queue();
            return;
        }

        hook.sendMessage("?? You are in position " + user.position() + "!").setEphemeral(true).queue();
    }
}
