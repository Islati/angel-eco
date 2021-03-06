package io.gridmc.angel.commands;

import io.gridmc.angel.api.commands.Command;
import io.gridmc.angel.api.commands.CommandInfo;
import io.gridmc.angel.listeners.AngelPlayer;
import io.gridmc.angel.listeners.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandInfo(name = "balance", aliases = "bal", description = "")
public class BalanceCommand {

    @Command(identifier = "bal", description = "View your balance")
    public void onBalanceCommand(Player player) {
        AngelPlayer ap = PlayerManager.getInstance().getPlayerData(player.getUniqueId());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&aYou have &e$%s&a.", ap.getBalance())));
    }
}
