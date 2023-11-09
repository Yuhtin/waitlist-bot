package com.yuhtin.quotes.waitlistbot.util;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
public final class DiscordUtil {

    public static String normalizeDiscordName(String discord) {
        if (discord == null || discord.isBlank()) return null;

        String normalized = discord.contains("#") ? discord.split("#")[0] : discord;
        return normalized.toLowerCase();
    }

}
