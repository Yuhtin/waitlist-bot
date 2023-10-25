package com.yuhtin.quotes.waitlistbot.config;

import com.yuhtin.quotes.waitlistbot.util.Serializer;
import lombok.Getter;

import java.io.*;
import java.util.logging.Logger;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Getter
public class Config {

    private static final Logger logger = Logger.getLogger("WaitlistBot");

    private long subscribersCountChannelId;
    private long waitlistChatChannelId;
    private long accessRoleId;
    private long guildId;

    private String zootoolsListId = "G68eAPC5dtuuiXIfnoNw";
    private String zootoolsApiKey = "none";

    private String mongoAddress = "localhost";
    private String mongoLogin = "user:password";
    private String mongoDatabase = "test";

    private String redisAddress = "localhost";
    private String redisPassword = "test";

    private String readMeMessage = "read-me";
    private String remindMessage = "remind-message";

    private String userNotFound = "Sorry, we couldn't find your discord username on our waitlist signup. " +
            "Open our website again, click on the waitlist button, and then 'check your position'. " +
            "From there you should be able to see your invite link, position, and more!";

    private String inviteMessage = "Your invite link: %link%. " +
            "Users who invite only one friend jump several positions! " +
            "Creating games together on Soba is infinitely more fun. " +
            "Discord DMs is used a lot to invite them.";

    private String positionMessage = "%user%, your waitlist position: #%position%";

    private String achievedAccessRoleMessage = "%user% invited two friends and has now access to more Discord channels!";

    private String joinedWithReferralMessage = "%user% just signed up with the invite link of %referralOwner% " +
            "(use /invite to get your link and refer friends)!";

    private String token = "none";

    public static Config loadConfig(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {

                if (!file.createNewFile()) return null;

                Config config = new Config();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
                    writer.write(Serializer.CONFIG.serialize(config));
                    writer.newLine();
                    writer.flush();
                }

                logger.severe("Config not found, creating a new config!");
                logger.severe("Put a valid token in the bot's config");
                return null;
            }

            String line;
            StringBuilder responseContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                while ((line = reader.readLine()) != null) responseContent.append(line);
            }

            return Serializer.CONFIG.deserialize(responseContent.toString());
        } catch (Exception exception) {
            return null;
        }
    }

}
