package com.yuhtin.quotes.waitlistbot.repository;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.yuhtin.quotes.waitlistbot.repository.mongo.MongoRepository;
import com.yuhtin.quotes.waitlistbot.repository.mongo.RepositoryCollectionField;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
@Getter
@Accessors(fluent = true)
public class MongoClientManager {

    private final Logger logger = Logger.getLogger("WaitlistBot");
    private static final MongoClientManager INSTANCE = new MongoClientManager();
    private final List<MongoRepository<?>> repositories = new ArrayList<>();

    private MongoDatabase database;
    private MongoClient client;

    public static MongoClientManager getInstance() {
        return INSTANCE;
    }

    public void load(String mongoUri, String databaseName) {

        logger.info("Starting MongoClient");
        try {

            client = MongoClients.create(mongoUri);
            client.startSession();

            database = client.getDatabase(databaseName);
            logger.info("Connected to database " + database.getName());
        } catch (MongoException exception) {
            exception.printStackTrace();
            logger.severe("Error trying to start MongoClient -> " + exception.getMessage());
        }

    }

    public void close() {
        if (client != null) {
            client.close();
            logger.info("Closed connection");
        }
    }

    public void injectTables(MongoRepository<?>... tables) {
        for (val table : tables) {
            injectTable(table);
        }

        logger.info("Loaded " + repositories.size() + " repository handlers successfully");
    }

    private void injectTable(MongoRepository<?> mongoRepository) {

        val collectionFields = getCollectionFields(mongoRepository);

        for (RepositoryCollectionField repositoryCollectionField : collectionFields) {

            val collectionName = repositoryCollectionField.getCollectionAnnotation().collectionName();
            repositoryCollectionField.accessible();

            val collection = database.getCollection(collectionName);
            logger.info("Loaded collection " + database.getName() + "/" + collectionName);

            injectValue(mongoRepository, repositoryCollectionField, collection);

        }

        repositories.add(mongoRepository);
    }

    private void injectValue(MongoRepository<?> injectable, RepositoryCollectionField repositoryCollectionField, Object value) {
        try {
            Field holder = repositoryCollectionField.getHolder();
            holder.set(injectable, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private List<RepositoryCollectionField> getCollectionFields(MongoRepository<?> object) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .map(RepositoryCollectionField::new)
                .filter(t -> t.getCollectionAnnotation() != null)
                .collect(Collectors.toList());
    }

}
