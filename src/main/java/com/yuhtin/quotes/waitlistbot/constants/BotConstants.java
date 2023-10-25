package com.yuhtin.quotes.waitlistbot.constants;

public class BotConstants {

    public static final String MONGO_URI = "mongodb://$login$@$address$/admin?retryWrites=true&w=majority&appName=mongosh+1.5.4";
    public static final String ENTERPRISE_ICON_URL = "https://www.soba.xyz/img/icons/favicon.ab6fbc.svg";

    public static final String ZOOTOOLS_GET_USER_ENDPOINT = "https://audience-api.zootools.co/lists/{listId}/members/{memberId}";
    public static final String ZOOTOOLS_GIVE_POINTS_ENDPOINT = "https://audience-api.zootools.co/v1/lists/{listId}/members/{memberId}/increase-points";

}
