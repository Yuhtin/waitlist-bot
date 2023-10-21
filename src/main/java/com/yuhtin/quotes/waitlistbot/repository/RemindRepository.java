package com.yuhtin.quotes.waitlistbot.repository;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.yuhtin.quotes.waitlistbot.model.RemindUser;
import com.yuhtin.quotes.waitlistbot.repository.mongo.MongoRepository;
import com.yuhtin.quotes.waitlistbot.repository.mongo.OperationType;
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
public class RemindRepository implements MongoRepository<RemindUser> {

    @Getter
    private static final RemindRepository instance = new RemindRepository();
    private static final Gson GSON = new GsonBuilder().create();

    @RepositoryCollection(collectionName = "remind_users")
    private MongoCollection<Document> userTable;

    @Nullable
    @Override
    public RemindUser find(String memberId) {
        val document = userTable.find(Filters.eq("_id", memberId)).first();
        if (document == null) return null;

        return GSON.fromJson(document.toJson(), RemindUser.class);
    }

    @Override
    public OperationType insert(RemindUser data) {
        RemindUser user = find(String.valueOf(data._id()));
        if (user != null) {
            replace(data);
            return OperationType.REPLACE;
        }

        userTable.insertOne(Document.parse(GSON.toJson(data)));
        return OperationType.INSERT;
    }

    @Override
    public void replace(RemindUser data) {
        userTable.replaceOne(Filters.eq("_id", data._id()), Document.parse(GSON.toJson(data)));
    }

    @Override
    public void delete(String memberId) {
        userTable.deleteOne(Filters.eq("_id", memberId));
    }

    @Override
    public LinkedList<RemindUser> query(int maxValues) {
        val documents = Lists.newArrayList(userTable.find());
        if (documents.isEmpty()) return Lists.newLinkedList();

        LinkedList<RemindUser> users = Lists.newLinkedList();
        for (Document document : documents) {
            val json = document.toJson();
            users.add(GSON.fromJson(json, RemindUser.class));
        }

        return users;
    }
}
