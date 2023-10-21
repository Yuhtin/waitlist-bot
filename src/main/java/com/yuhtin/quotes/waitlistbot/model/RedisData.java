package com.yuhtin.quotes.waitlistbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;

@Getter
@AllArgsConstructor
public class RedisData {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private final String id;
    private final String email;
    private final String discord;
    private final String listId;
    private final String referralUserId;
    private final int position;
    private final int subscribersCount;

}
