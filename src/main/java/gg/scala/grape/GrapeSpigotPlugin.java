package gg.scala.grape;

import com.mongodb.client.model.Filters;
import gg.scala.commons.ExtendedScalaPlugin;
import gg.scala.commons.annotations.commands.ManualRegister;
import gg.scala.commons.annotations.container.ContainerEnable;
import gg.scala.commons.command.ScalaCommandManager;
import gg.scala.grape.command.CoinsCommand;
import gg.scala.grape.handler.PlayerHandler;
import gg.scala.grape.handler.StorageHandler;
import gg.scala.grape.player.impl.PersistentGrapePlayer;
import lombok.Getter;
import me.lucko.helper.Events;
import org.bson.Document;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
public final class GrapeSpigotPlugin extends ExtendedScalaPlugin {

    @Getter
    private static GrapeSpigotPlugin instance;

    private StorageHandler storageHandler;
    private PlayerHandler playerHandler;

    @ContainerEnable
    public void containerEnable() {
        instance = this;

        this.storageHandler = new StorageHandler();
        this.playerHandler = new PlayerHandler(this);

        this.setupListeners();
    }

    @ManualRegister
    public void setupCommands(ScalaCommandManager manager) {
        manager.registerCommand(new CoinsCommand());
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
