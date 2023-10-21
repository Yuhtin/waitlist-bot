package com.yuhtin.quotes.waitlistbot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public class RemindUser {

    private final long _id;
    private final long remindMillis;

}
