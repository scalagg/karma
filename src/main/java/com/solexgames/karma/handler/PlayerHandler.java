package com.solexgames.karma.handler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.mongodb.client.model.Filters;
import com.solexgames.karma.KarmaSpigotPlugin;
import com.solexgames.karma.player.impl.CachedKarmaPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
@RequiredArgsConstructor
@SuppressWarnings("all")
public class PlayerHandler {

    private final KarmaSpigotPlugin plugin;

    private final Gson gson = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .disableHtmlEscaping().create();

    private final LoadingCache<UUID, CachedKarmaPlayer> cachedKarmaPlayerLoadingCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60L, TimeUnit.SECONDS)
            .removalListener(notification -> {
                this.plugin.getLogger().info("Now removing " + notification.getKey() + " from the cache.");
            })
            .build(CacheLoader.asyncReloading(new CacheLoader<UUID, CachedKarmaPlayer>() {
                @Override
                public CachedKarmaPlayer load(@NotNull UUID key) {
                    final Document document = PlayerHandler.this.plugin.getStorageHandler().getPlayerCollection()
                            .find(Filters.eq("uuid", key.toString())).first();

                    return document == null ? null : PlayerHandler.this.gson.fromJson(document.toJson(), CachedKarmaPlayer.class);
                }
            }, ForkJoinPool.commonPool()));

}
