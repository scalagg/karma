package gg.scala.grape.player.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import gg.scala.grape.GrapeSpigotPlugin;
import gg.scala.grape.model.PersistentModel;
import gg.scala.grape.player.GrapePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter @Setter
@RequiredArgsConstructor
public class PersistentGrapePlayer extends GrapePlayer implements PersistentModel<Document> {

    private final String name;
    private final UUID uniqueId;

    private int karma;
    private int coins;

    @Override
    public void load(CompletableFuture<Document> documentCompletableFuture) {
        documentCompletableFuture.whenComplete((document, throwable) -> {
            if (document == null) {
                this.save().whenComplete((unused, throwable1) -> {
                    if (throwable1 != null) {
                        throwable1.printStackTrace();
                    }
                });

                return;
            }

            this.coins = document.getInteger("coins");
            this.karma = document.getInteger("karma");
        });
    }

    @Override
    public CompletableFuture<Void> save() {
        return CompletableFuture.runAsync(() -> {
            GrapeSpigotPlugin.getInstance().getStorageHandler().getPlayerCollection().replaceOne(
                    Filters.eq("uuid", this.uniqueId.toString()),
                    this.getDocument(), new ReplaceOptions().upsert(true)
            );
        });
    }

    public void setCoins(int amount) {
        this.coins = amount;

        this.save();
    }

    public Document getDocument() {
        final Document document = new Document("_id", this.uniqueId);

        document.put("uuid", this.uniqueId.toString());
        document.put("coins", this.coins);
        document.put("karma", this.karma);

        return document;
    }
}
