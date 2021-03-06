package io.gridmc.angel.commands;

import io.gridmc.angel.api.commands.Arg;
import io.gridmc.angel.api.commands.Command;
import io.gridmc.angel.api.commands.CommandInfo;
import io.gridmc.angel.api.commands.HelpScreen;
import io.gridmc.angel.listeners.AngelPlayer;
import io.gridmc.angel.listeners.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "eco", aliases = "ae", description = "Manage your balance", usage = "/eco")
public class EconomyCommand {

    private HelpScreen help = new HelpScreen("Economy Command Help")
            .addEntry("eco", "Base Command // Opens this command menu")
            .addEntry("eco bal", "View a players balance")
            .addEntry("eco give", "Give players money", "gridmc.admin")
            .addEntry("bal", "View your balance");

    public EconomyCommand() {

    }

    @Command(identifier = "eco", description = "Economy base command", onlyPlayers = false)
    public void onEcoBaseCommand(CommandSender sender) {
        help.sendTo(sender, 1, 10);
    }


    @Command(identifier = "eco bal", description = "View a players balance", onlyPlayers = false)
    public void onEcoBalanceCommand(CommandSender sender, @Arg(name = "player") Player player) {
        //todo retrieve player balance and send to individual executing the command.
        PlayerManager manager = PlayerManager.getInstance();

        AngelPlayer target = manager.getPlayerData(player.getUniqueId());

        if (target == null) {
            sender.sendMessage("Unable to find player data for " + player.getName());
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&b%s&a has &e$%s&a.", player.getName(), target.getBalance())));
    }

    @Command(identifier = "eco give", description = "Give players money", onlyPlayers = false, permissions = {"gridmc.admin"})
    public void onEcoGiveCommand(CommandSender sender, @Arg(name = "player") Player player, @Arg(name = "amount") int amount) {
        //award currency (+)

        AngelPlayer target = PlayerManager.getInstance().getPlayerData(player.getUniqueId());
        if (target == null) {
            sender.sendMessage("Unable to find player data for " + player.getName());
            return;
        }

        target.balanceAdd(amount);

        /* Send balance message */
        if (sender instanceof Player) {
            Player pSender = (Player)sender;
            if (!pSender.getUniqueId().equals(player.getUniqueId())) {
                onEcoBalanceCommand(sender, player);
            }
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',String.format("&aYou now have &e$%s&a.", target.getBalance())));
    }

    @Command(identifier = "eco set", description = "Set players money", onlyPlayers = false, permissions = {"gridmc.admin"})
    public void onEcoSetCommand(CommandSender sender, @Arg(name = "player") Player player, @Arg(name = "amount") int amount) {
        //set a player to have a specific amt of currency.

        AngelPlayer target = PlayerManager.getInstance().getPlayerData(player.getUniqueId());
        if (target == null) {
            sender.sendMessage("Unable to find player data for " + player.getName());
            return;
        }

        target.balanceSet(amount);
        if (sender instanceof Player) {
            Player pSender = (Player)sender;
            if (!pSender.getUniqueId().equals(player.getUniqueId())) {
                onEcoBalanceCommand(sender, player);
            }
        }
        player.sendMessage(String.format("&aYou now have &e$%s&a.", target.getBalance()));
    }
}
