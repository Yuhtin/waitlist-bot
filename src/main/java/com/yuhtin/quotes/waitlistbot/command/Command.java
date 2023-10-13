package com.yuhtin.quotes.waitlistbot.command;

import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public interface Command {

    void execute(CommandInteraction command, InteractionHook hook) throws Exception;

}
