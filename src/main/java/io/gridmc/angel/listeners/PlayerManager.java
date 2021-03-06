package io.gridmc.angel.listeners;

import io.gridmc.angel.Angel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles the saving and loading of player data.
 */
public class PlayerManager implements Listener {

    private final Map<UUID, AngelPlayer> angelPlayerData = new HashMap<>();

    private static PlayerManager instance;

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }

        return instance;
    }

    protected PlayerManager() {

    }

    public AngelPlayer getPlayerData(UUID uid) {
        return angelPlayerData.get(uid);
    }

    public boolean hasPlayerData(UUID uid) {
        return angelPlayerData.containsKey(uid);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        AngelPlayer player = new AngelPlayer(e.getPlayer());
        angelPlayerData.put(player.getUuid(), player);

        Bukkit.getScheduler().runTaskAsynchronously(Angel.getInstance(), new Runnable() {
            @Override
            public void run() {
                player.load();
                Angel.getInstance().getLogger().info("Loaded database information for " + player.getName());
            }
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        onPlayerLeave(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e) {
        onPlayerLeave(e.getPlayer());
    }

    public void onPlayerLeave(Player player) {

        AngelPlayer angelPlayer = angelPlayerData.get(player.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(Angel.getInstance(), new Runnable() {
            @Override
            public void run() {
                angelPlayer.save();
                Angel.getInstance().getLogger().info("Saved data for " + player.getName() + " to the database.");
            }
        });
    }
}
