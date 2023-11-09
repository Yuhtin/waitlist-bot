package com.yuhtin.quotes.waitlistbot.command.impl;

import com.yuhtin.quotes.waitlistbot.WaitlistBot;
import com.yuhtin.quotes.waitlistbot.command.Command;
import com.yuhtin.quotes.waitlistbot.command.CommandInfo;
import com.yuhtin.quotes.waitlistbot.config.Config;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.UserRepository;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

@CommandInfo(
        names = {"position"},
        description = "Shows your waitlist position"
)
public class PositionCommand implements Command {

    @Override
    public void execute(CommandInteraction command) {
        ReplyCallbackAction callback = command.deferReply();
        Config config = WaitlistBot.getInstance().getConfig();

        User user = UserRepository.instance().findByDiscordName(command.getUser().getName());
        if (user == null) {
            callback.setEphemeral(true)
                    .queue(hook -> hook.sendMessage(config.getUserNotFound()).queue());
            return;
        }

        if (user.discordId() == 0) {
            user.discordId(command.getUser().getIdLong());
            user.save();
        }

        callback.queue(hook -> hook.sendMessage(config.getPositionMessage()
                .replace("%user%", command.getUser().getAsMention())
                .replace("%position%", String.valueOf(user.position()))
        ).queue());
    }
}
