package com.yuhtin.quotes.waitlistbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@AllArgsConstructor
public class WebhookData {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final String id;
    private final String email;
    private final String discord;
    private final String subscriptionDate;
    private final int position;
    private final int subscribersCount;

    public long getSubscriptionDate() {
        try {
            return DATE_FORMAT.parse(subscriptionDate).getTime();
        } catch (Exception e) {
            return -1;
        }
    }

}
