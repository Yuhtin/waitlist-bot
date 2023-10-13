package com.yuhtin.quotes.waitlistbot.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public final class CommandCatcher extends ListenerAdapter {

    private static final CommandCatcher INSTANCE = new CommandCatcher(new CommandMap());
    private final CommandMap commandMap;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        InteractionHook hook = event.deferReply().complete();

        Command command = commandMap.getCommands().get(event.getName());
        try {
            command.execute(event, hook);
        } catch (Exception exception) {
            exception.printStackTrace();
            hook.sendMessage("ERRO!").queue();
        }
    }

    public static CommandCatcher getInstance() {
        return INSTANCE;
    }
}
