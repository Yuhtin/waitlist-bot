package com.yuhtin.quotes.waitlistbot.repository;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yuhtin.quotes.waitlistbot.model.User;
import com.yuhtin.quotes.waitlistbot.repository.mongo.MongoRepository;
import com.yuhtin.quotes.waitlistbot.repository.mongo.RepositoryCollection;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

/**
 * @author <a href="https://github.com/Yuhtin">Yuhtin</a>
 */
@Accessors(fluent = true)
public class UserRepository implements MongoRepository<User> {

    @Getter
    private static final UserRepository instance = new UserRepository();
    private static final Gson GSON = new GsonBuilder().create();

    @RepositoryCollection(collectionName = "users")
    private MongoCollection<Document> userTable;

    @Nullable
    @Override
    public User find(String memberId) {
        val document = userTable.find(Filters.eq("memberId", memberId)).first();
        if (document == null) return null;

        return GSON.fromJson(document.toJson(), User.class);
    }

    @Nullable
    public User findByDiscordName(String discordName) {
        val document = userTable.find(Filters.eq("discordName", discordName.toLowerCase())).first();
        if (document == null) return null;

        return GSON.fromJson(document.toJson(), User.class);
    }

    @Override
    public void insert(User data) {
        if (find(data.memberId()) != null) {
            replace(data);
            return;
        }

        userTable.insertOne(Document.parse(GSON.toJson(data)));
    }

    @Override
    public void replace(User data) {
        userTable.replaceOne(Filters.eq("memberId", data.memberId()), Document.parse(GSON.toJson(data)));
    }

    @Override
    public void delete(String memberId) {
        userTable.deleteOne(Filters.eq("memberId", memberId));
    }

    @Override
    public LinkedList<User> query(int maxValues) {
        val documents = Lists.newArrayList(userTable.find().sort(new BasicDBObject("referrals", -1)).limit(maxValues).iterator());
        if (documents.isEmpty()) return Lists.newLinkedList();

        LinkedList<User> users = Lists.newLinkedList();
        for (Document document : documents) {
            val json = document.toJson();
            users.add(GSON.fromJson(json, User.class));
        }

        return users;
    }
}
