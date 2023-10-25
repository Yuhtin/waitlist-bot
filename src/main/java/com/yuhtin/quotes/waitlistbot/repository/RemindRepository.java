package com.yuhtin.quotes.waitlistbot.repository;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.waitlistbot.model.RemindUser;
import com.yuhtin.quotes.waitlistbot.repository.adapters.RemindUserAdapter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@NoArgsConstructor
public final class RemindRepository {


    private static final RemindRepository INSTANCE = new RemindRepository();
    private static final String TABLE = "waitlist_reminders";

    private SQLExecutor sqlExecutor;

    public void init(SQLExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
        createTable();
    }

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "userId LONG NOT NULL PRIMARY KEY," +
                "remindMillis LONG DEFAULT -1" +
                ");"
        );
    }

    private RemindUser findRemindDelay(long userId) {
        return sqlExecutor.resultOneQuery(
                "SELECT * FROM " + TABLE + " WHERE userId = '" + userId + "'",
                statement -> {},
                RemindUserAdapter.class
        );
    }

    public Set<RemindUser> selectAll() {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE,
                k -> {},
                RemindUserAdapter.class
        );
    }

    public void insert(RemindUser data) {
        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?, ?)", TABLE),
                statement -> {
                    statement.set(1, data.userId());
                    statement.set(2, data.remindMillis());
                }
        );
    }

    public void delete(long userId) {
        this.sqlExecutor.updateQuery(
                String.format("DELETE FROM %s WHERE userId = ?", TABLE),
                statement -> statement.set(1, userId)
        );
    }

    public static RemindRepository instance() {
        return INSTANCE;
    }

}
