package com.solexgames.grape;

import com.mongodb.client.model.Filters;
import com.solexgames.grape.command.CoinsCommand;
import com.solexgames.grape.command.KarmaCommand;
import com.solexgames.grape.handler.PlayerHandler;
import com.solexgames.grape.handler.StorageHandler;
import com.solexgames.grape.player.impl.PersistentGrapePlayer;
import com.solexgames.lib.acf.PaperCommandManager;
import com.solexgames.shop.ShopSpigotPlugin;
import com.solexgames.shop.currency.impl.GrapeCurrencyHandler;
import me.lucko.helper.Events;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bson.Document;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
public final class GrapeSpigotPlugin extends ExtendedJavaPlugin {

    @Getter
    private static GrapeSpigotPlugin instance;

    private StorageHandler storageHandler;
    private PlayerHandler playerHandler;

    @Override
    protected void enable() {
        instance = this;

        this.storageHandler = new StorageHandler();
        this.playerHandler = new PlayerHandler(this);

        this.setupListeners();
        this.setupCommands();

        ShopSpigotPlugin.getInstance().setCurrencyHandler(new GrapeCurrencyHandler());
    }

    private void setupCommands() {
        final PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.enableUnstableAPI("help");

        paperCommandManager.registerCommand(new CoinsCommand());
        paperCommandManager.registerCommand(new KarmaCommand());
    }

    private void setupListeners() {
        Events.subscribe(AsyncPlayerPreLoginEvent.class).handler(event -> {
            final PersistentGrapePlayer player = new PersistentGrapePlayer(
                    event.getName(),
                    event.getUniqueId()
            );

            final CompletableFuture<Document> completableFuture = CompletableFuture.supplyAsync(() ->
                    this.storageHandler.getPlayerCollection().find(Filters.eq("uuid", event.getUniqueId().toString())).first()
            );

            player.load(completableFuture);

            this.playerHandler.getPlayerTypeMap().put(event.getUniqueId(), player);
        });

        Events.subscribe(PlayerQuitEvent.class).handler(playerQuitEvent -> {
            final PersistentGrapePlayer player = this.playerHandler.getByPlayer(playerQuitEvent.getPlayer());

            if (player != null) {
                player.save();

                this.playerHandler.getPlayerTypeMap().remove(player.getUniqueId());
            }
        });
    }
}
