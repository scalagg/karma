package gg.scala.grape.handler;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import gg.scala.store.ScalaDataStoreShared;
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
        this.client = ScalaDataStoreShared.getINSTANCE()
                .getNewMongoConnection().getConnection();

        this.database = this.client.getDatabase("Scala");
        this.playerCollection = this.database.getCollection("GrapePlayer");
    }
}
