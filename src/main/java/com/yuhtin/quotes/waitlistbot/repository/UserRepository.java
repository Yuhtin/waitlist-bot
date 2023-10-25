package com.yuhtin.quotes.waitlistbot.repository;

import com.henryfabio.sqlprovider.executor.SQLExecutor;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.adapters.UserAdapter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@NoArgsConstructor
public final class UserRepository {


    private static final UserRepository INSTANCE = new UserRepository();
    private static final String TABLE = "waitlist_users";

    private SQLExecutor sqlExecutor;

    public void init(SQLExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
        createTable();
    }

    public void createTable() {
        sqlExecutor.updateQuery("CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
                "memberId LONGTEXT NOT NULL PRIMARY KEY," +
                "email LONGTEXT NOT NULL," +
                "discordName LONGTEXT NULL," +
                "position INTEGER NOT NULL DEFAULT -1," +
                "discordId LONG DEFAULT 0," +
                "referrals INTEGER DEFAULT 0," +
                "messagesInChat INTEGER DEFAULT 0" +
                ");"
        );
    }

    private User selectOneQuery(String query) {
        return sqlExecutor.resultOneQuery(
                "SELECT * FROM " + TABLE + " " + query,
                statement -> {
                },
                UserAdapter.class
        );
    }

    public User findByEmail(String owner) {
        return selectOneQuery("WHERE email = '" + owner + "'");
    }

    public User findByDiscordId(long discordId) {
        return selectOneQuery("WHERE discordId = " + discordId);
    }

    public User findByMemberId(String memberId) {
        return selectOneQuery("WHERE memberId = '" + memberId + "'");
    }

    public User findByDiscordName(String discordName) {
        return selectOneQuery("WHERE discordName = '" + discordName + "'");
    }

    public Set<User> selectAll(String query) {
        return sqlExecutor.resultManyQuery(
                "SELECT * FROM " + TABLE + " " + query,
                k -> {
                },
                UserAdapter.class
        );
    }

    public OperationType insert(User data) {
        User user = findByMemberId(data.memberId());

        this.sqlExecutor.updateQuery(
                String.format("REPLACE INTO %s VALUES(?, ?, ?, ?, ?, ?, ?)", TABLE),
                statement -> {
                    statement.set(1, data.memberId());
                    statement.set(2, data.email());
                    statement.set(3, data.discordName());
                    statement.set(4, data.position());
                    statement.set(5, data.retrieveDiscordId());
                    statement.set(6, data.referrals());
                    statement.set(7, data.messagesInChat());
                }
        );

        return user == null ? OperationType.INSERT : OperationType.UPDATE;
    }

    public static UserRepository instance() {
        return INSTANCE;
    }

}
