package com.solexgames.karma;

import com.solexgames.karma.handler.PlayerHandler;
import com.solexgames.karma.handler.StorageHandler;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

/**
 * @author GrowlyX
 * @since 8/13/2021
 */

@Getter
public final class KarmaSpigotPlugin extends ExtendedJavaPlugin {

    @Getter
    private static KarmaSpigotPlugin instance;

    private final StorageHandler storageHandler = new StorageHandler();
    private final PlayerHandler playerHandler = new PlayerHandler(this);

    @Override
    protected void enable() {
        instance = this;
    }
}
