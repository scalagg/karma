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

@CommandAlias("coins")
public class CoinsCommand extends BaseCommand {

    @Default
    @Syntax("[optional: <target>]")
    @Description("View a specific player's balance.")
    public void onDefault(Player player, @Optional OnlinePlayer target) {
        final PersistentGrapePlayer grapePlayer = GrapeSpigotPlugin.getInstance().getPlayerHandler()
                .getByPlayer(target != null ? target.getPlayer() : player);

        player.sendMessage(ChatColor.GOLD + (target != null ? target.getPlayer().getName() + " has" : "You have") + " " + grapePlayer.getCoins() + " coins.");
    }

    @HelpCommand
    @Syntax("[page]")
    public void onHelp(CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("pay")
    @Syntax("<target> [amount]")
    @CommandCompletion("@players")
    @Description("Send another player coins out of your own balance.")
    public void onPay(Player player, OnlinePlayer target, int amount) {
        final PersistentGrapePlayer playerGrapePlayer = GrapeSpigotPlugin.getInstance()
                .getPlayerHandler().getByPlayer(player);
        final PersistentGrapePlayer targetGrapePlayer = GrapeSpigotPlugin.getInstance()
                .getPlayerHandler().getByPlayer(target.getPlayer());

        if (player.getUniqueId().equals(target.getPlayer().getUniqueId())) {
            throw new ConditionFailedException("You cannot pay yourself.");
        }

        if (playerGrapePlayer.getCoins() < amount) {
            throw new ConditionFailedException("You don't have " + ChatColor.YELLOW + amount + ChatColor.RED + " coins.");
        }

        targetGrapePlayer.setCoins(targetGrapePlayer.getCoins() + amount);
        playerGrapePlayer.setCoins(playerGrapePlayer.getCoins() - amount);

        player.sendMessage(ChatColor.YELLOW + "You gave " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " " + ChatColor.GREEN + amount + " coins" + ChatColor.YELLOW + ".");
        target.getPlayer().sendMessage(ChatColor.YELLOW + "You've received " + ChatColor.GREEN + amount + " coins" + ChatColor.YELLOW + " from " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + ".");
    }

    @Subcommand("give|add")
    @Syntax("<target> [amount]")
    @CommandCompletion("@players")
    @CommandPermission("grape.command.coins.give")
    @Description("Add coins to a player's balance.")
    public void onGive(Player player, OnlinePlayer target, int amount) {
        final PersistentGrapePlayer targetGrapePlayer = GrapeSpigotPlugin.getInstance()
                .getPlayerHandler().getByPlayer(target.getPlayer());

        targetGrapePlayer.setCoins(targetGrapePlayer.getCoins() + amount);

        player.sendMessage(ChatColor.YELLOW + "You've given " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " " + ChatColor.GREEN + amount + " coins" + ChatColor.YELLOW + ".");
        target.getPlayer().sendMessage(ChatColor.YELLOW + "You've received " + ChatColor.GREEN + amount + " coins" + ChatColor.YELLOW + ".");
    }

    @Subcommand("remove")
    @Syntax("<target> [amount]")
    @CommandCompletion("@players")
    @CommandPermission("grape.command.coins.remove")
    @Description("Remove coins from a player's balance.")
    public void onRemove(Player player, OnlinePlayer target, int amount) {
        final PersistentGrapePlayer targetGrapePlayer = GrapeSpigotPlugin.getInstance()
                .getPlayerHandler().getByPlayer(target.getPlayer());

        targetGrapePlayer.setCoins(targetGrapePlayer.getCoins() - amount);

        player.sendMessage(ChatColor.YELLOW + "You removed " + ChatColor.GREEN + amount + " coins" + ChatColor.YELLOW + " from " + ChatColor.GOLD + player.getName() + ChatColor.YELLOW + "'s balance.");
    }
}
