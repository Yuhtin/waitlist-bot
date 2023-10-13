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

    private long webhookChannelId;
    private long subscribersCountChannelId;

    private String mongoAddress = "localhost";
    private String mongoLogin = "user@password";
    private String mongoDatabase = "test";
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
