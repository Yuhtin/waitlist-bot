package com.yuhtin.quotes.waitlistbot.command.impl;

import com.yuhtin.quotes.waitlistbot.command.Command;
import com.yuhtin.quotes.waitlistbot.command.CommandInfo;
import com.yuhtin.quotes.waitlistbot.constants.BotConstants;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@CommandInfo(name = "waitlist", description = "Shows your waitlist stats")
public class WaitlistCommand implements Command {

    @Override
    public void execute(CommandInteraction command, InteractionHook hook) {
        User user = UserRepository.instance().findByDiscordName(command.getUser().getName());
        if (user == null) {
            hook.sendMessage("You are not in the waitlist!").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor("Waitlist Stats", null, "https://cdn-icons-png.flaticon.com/512/10466/10466166.png");
        embedBuilder.setFooter("Soba Studios © 2023", BotConstants.ENTERPRISE_ICON_URL);
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setDescription(
                "Email: " + user.email() + "\n" +
                        "Creation Date: <t:" + TimeUnit.MILLISECONDS.toSeconds(user.creationDate()) + ":R>\n" +
                        "Referred Users: " + user.referrals()
        );

        hook.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
