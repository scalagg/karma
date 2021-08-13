package com.solexgames.karma.handler;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.solexgames.core.CorePlugin;
import lombok.Getter;
import org.bson.Document;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
public class StorageHandler {

    private final MongoClient client;
    private final MongoDatabase database;

    private final MongoCollection<Document> playerCollection;

    public StorageHandler() {
        this.client = CorePlugin.getInstance().getCoreDatabase().getClient();

        this.database = this.client.getDatabase("SGSoftware");
        this.playerCollection = this.database.getCollection("karma_players");
    }
}
