package com.yuhtin.quotes.waitlistbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@AllArgsConstructor
public class WebhookData {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final DataActionType actionType;
    private final String memberId;
    private final String memberEmail;
    private final String discordName;
    private final String creationDate;
    private final int subscribersCount;

    public long getCreationDate() {
        try {
            return DATE_FORMAT.parse(creationDate).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

}
