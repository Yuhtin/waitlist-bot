package com.yuhtin.quotes.waitlistbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class WaitlistCommand extends ListenerAdapter {

    private static final String enterpriseIconUrl = "https://scontent.fgig14-2.fna.fbcdn.net/v/t39.30808-6/355911136_675466524595263_4137884340159981443_n.png?_nc_cat=110&ccb=1-7&_nc_sid=a2f6c7&_nc_ohc=UHDfUY6vOsEAX-gi7iF&_nc_ht=scontent.fgig14-2.fna&oh=00_AfCQRhHhj-kQ9BDcXYcyY8CFWhxMStZ5D70IoeXp5-w7rw&oe=6527C705";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent command) {
        SlashCommandInteraction event = command.getInteraction();
        if (!event.getCommandId().equalsIgnoreCase("waitlist")) return;

        event.deferReply()
                .setEphemeral(true)
                .queue(interaction -> {
                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    embedBuilder.setAuthor("Waitlist Stats", "", "https://cdn-icons-png.flaticon.com/512/10466/10466166.png");
                    embedBuilder.setColor(Color.CYAN);
                    embedBuilder.setFooter("Goodgame Studios © 2023", enterpriseIconUrl);
                    embedBuilder.setDescription(
                            ""
                    );
                });

    }
}
