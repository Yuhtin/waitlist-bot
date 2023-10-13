package com.yuhtin.quotes.waitlistbot.repository.mongo;

import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public interface MongoRepository<T> {

    @Nullable T find(String memberId);

    void insert(T data);

    void replace(T data);

    void delete(String memberId);

    LinkedList<T> query(int maxValues);

}
