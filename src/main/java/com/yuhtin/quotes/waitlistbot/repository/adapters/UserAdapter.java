package com.yuhtin.quotes.waitlistbot.repository.adapters;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.quotes.waitlistbot.model.User;

public class UserAdapter implements SQLResultAdapter<User> {

    @Override
    public User adaptResult(SimpleResultSet resultSet) {
        return User.builder()
                .memberId(resultSet.get("memberId"))
                .email(resultSet.get("email"))
                .discordName(resultSet.get("discordName"))
                .position(resultSet.get("position"))
                .discordId(resultSet.get("discordId"))
                .referrals(resultSet.get("referrals"))
                .messagesInChat(resultSet.get("messagesInChat"))
                .build();
    }

}
