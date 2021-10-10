package gg.scala.grape.model;

import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 8/9/2021
 */

public interface PersistentModel<K> {

    void load(CompletableFuture<K> k);

    CompletableFuture<Void> save();

}
