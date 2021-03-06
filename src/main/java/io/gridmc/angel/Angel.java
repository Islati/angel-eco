package io.gridmc.angel;

import io.gridmc.angel.api.yml.YMLIO;
import io.gridmc.angel.commands.BalanceCommand;
import io.gridmc.angel.commands.EconomyCommand;
import io.gridmc.angel.api.commands.CommandHandler;
import io.gridmc.angel.db.DatabaseController;
import io.gridmc.angel.listeners.PlayerManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Angel extends JavaPlugin {


    /*
    Startup Logic:
        1. Database Connection
        2. Verify existence
            2. (a) Create if not
        3. Register plugin commands.
        4. Register Listeners
     */

    @Getter
    private static Angel instance;

    private CommandHandler commandHandler = null;

    private YMLIO ymlConfig = null;

    @Getter
    private DatabaseController dbController = null;

    @Override
    public void onLoad() {
        super.onLoad();
        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        File pluginFolder = getDataFolder();

        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        /* Create command handler and register commands for Angel */
        commandHandler = new CommandHandler(this);
        commandHandler.registerCommands(new EconomyCommand(), new BalanceCommand());

        /*
        Handle the creation & loading of configuration.
         */


        File ymlConfigFile = new File(pluginFolder, "config.yml");
        try {
            ymlConfig = new YMLIO(ymlConfigFile);
            if (!ymlConfigFile.exists()) {
                ymlConfig.set("database.url", "jdbc:postgresql://<host>/dbname");
                ymlConfig.set("database.username", "user");
                ymlConfig.set("database.password", "password");
                ymlConfig.set("database.name", "dbname");

                ymlConfig.save();
                getLogger().info(String.format("Please configure config.yml at %s. Angel is now disabled.", ymlConfigFile.getAbsolutePath()));
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        } catch (IOException | InvalidConfigurationException e) {

            getLogger().info("You have an invalid config.yml file");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        /*
        Database connections.
         */
        dbController = new DatabaseController(ymlConfig.get("database.url", "jdbc:postgresql://<host>/<dbname>"), ymlConfig.get("database.username", "username"), ymlConfig.get("database.password", "password"), ymlConfig.get("database.name", "dbname"));

        if (!dbController.connect()) {
            getLogger().severe("Unable to connect to database; disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }


        if (!dbController.setup()) {
            getLogger().severe("Unable to setup database; disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        /*
        Register our listeners.
         */
        Bukkit.getPluginManager().registerEvents(PlayerManager.getInstance(), this);

        getLogger().info("Angel is now active!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
