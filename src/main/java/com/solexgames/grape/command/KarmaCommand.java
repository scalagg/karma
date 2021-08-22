package com.solexgames.grape.command;

import com.solexgames.grape.GrapeSpigotPlugin;
import com.solexgames.grape.player.impl.PersistentGrapePlayer;
import com.solexgames.lib.acf.BaseCommand;
import com.solexgames.lib.acf.CommandHelp;
import com.solexgames.lib.acf.ConditionFailedException;
import com.solexgames.lib.acf.annotation.*;
import com.solexgames.lib.acf.bukkit.contexts.OnlinePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 8/21/2021
 */


public class KarmaCommand extends BaseCommand {


    @CommandAlias("karma")
    @Syntax("[optional: <target>]")
    public void onDefault(Player player, @Optional OnlinePlayer target) {
        final PersistentGrapePlayer karmaPlayer = GrapeSpigotPlugin.getInstance().getPlayerHandler()
                .getByPlayer(target != null ? target.getPlayer() : player);

        player.sendMessage(ChatColor.LIGHT_PURPLE + (target != null ? target.getPlayer().getName() + " has" : "You have") + " " + karmaPlayer.getKarma() + " karma.");
    }
}
