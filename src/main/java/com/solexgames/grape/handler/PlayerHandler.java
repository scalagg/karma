package com.solexgames.grape.handler;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.mongodb.client.model.Filters;
import com.solexgames.grape.GrapeSpigotPlugin;
import com.solexgames.grape.commons.PlayerCache;
import com.solexgames.grape.player.impl.CachedGrapePlayer;
import com.solexgames.grape.player.impl.PersistentGrapePlayer;
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
public class PlayerHandler extends PlayerCache<PersistentGrapePlayer> {

    private final GrapeSpigotPlugin plugin;

    private final Gson gson = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .disableHtmlEscaping().create();

    private final LoadingCache<UUID, CachedGrapePlayer> cachedKarmaPlayerLoadingCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60L, TimeUnit.SECONDS)
            .build(CacheLoader.asyncReloading(new CacheLoader<UUID, CachedGrapePlayer>() {
                @Override
                public CachedGrapePlayer load(@NotNull UUID key) {
                    final Document document = PlayerHandler.this.plugin.getStorageHandler().getPlayerCollection()
                            .find(Filters.eq("uuid", key.toString())).first();

                    return document == null ? null : PlayerHandler.this.gson.fromJson(document.toJson(), CachedGrapePlayer.class);
                }
            }, ForkJoinPool.commonPool()));

}
