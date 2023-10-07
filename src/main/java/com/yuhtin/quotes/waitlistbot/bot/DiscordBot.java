package com.yuhtin.quotes.waitlistbot.bot;

import net.dv8tion.jda.api.JDA;

public interface DiscordBot {

    /**
     * Called before connecting to discord api
     *
     */
    void onEnable();

    /**
     * Called when the bot is ready and connected
     *
     */
    void onReady();

    /**
     * Receive the JDA instance
     *
     * @param jda the JDA instance
     */
    void serve(JDA jda);


}
