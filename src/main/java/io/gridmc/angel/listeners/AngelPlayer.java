package io.gridmc.angel.listeners;

import io.gridmc.angel.Angel;
import io.gridmc.angel.db.DatabaseController;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Player wrapper class to provide economy interfacing.
 */
public class AngelPlayer {

    @Getter
    private String name;
    @Getter
    private UUID uuid;
    @Getter
    private double balance;

    public AngelPlayer(Player player) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.balance = 5;
    }

    /**
     * Save player information to the database.
     * Recommended to call async to prevent lag.
     */
    public void save() {
        DatabaseController db = Angel.getInstance().getDbController();
        db.updatePlayerData(this);
    }


    /**
     * Load player information from the database.
     * Recommended to call async to prevent lag.
     */
    public void load() {
        DatabaseController db = Angel.getInstance().getDbController();
        if (!db.hasPlayerData(this)) {
            db.insertDefaultPlayerData(this);
        }

        this.balance = db.getPlayerBalance(this);
    }

    public void balanceAdd(double amount) {
        this.balance += amount;
    }

    public void balanceSubtract(double amount) {
        this.balance -= amount;
    }

    public void balanceSet(double amount) {
        this.balance = amount;
    }

}

