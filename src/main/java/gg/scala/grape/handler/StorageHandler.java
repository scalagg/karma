package gg.scala.grape.handler;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import gg.scala.lemon.Lemon;
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
        this.client = new MongoClient(
                new MongoClientURI(
                        Lemon.getInstance().getMongoConfig().getUri()
                )
        );

        this.database = this.client.getDatabase("SGSoftware");
        this.playerCollection = this.database.getCollection("karma_players");
    }
}
