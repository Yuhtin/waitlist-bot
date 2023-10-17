package com.yuhtin.quotes.waitlistbot.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Data
@Builder
@Accessors(fluent = true)
public class User {

    private final String memberId;
    private final String email;
    private final long creationDate;

    private String discordName;
    private int position;
    private int referrals;

}
