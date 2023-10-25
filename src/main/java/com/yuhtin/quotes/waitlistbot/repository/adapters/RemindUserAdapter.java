package com.yuhtin.quotes.waitlistbot.repository.adapters;

import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;
import com.yuhtin.quotes.waitlistbot.model.RemindUser;

public class RemindUserAdapter implements SQLResultAdapter<RemindUser> {

    @Override
    public RemindUser adaptResult(SimpleResultSet resultSet) {
        return new RemindUser(
                resultSet.get("userId"),
                resultSet.get("remindMillis")
        );
    }

}
