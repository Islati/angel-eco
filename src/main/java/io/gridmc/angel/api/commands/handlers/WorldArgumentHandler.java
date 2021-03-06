package io.gridmc.angel.api.commands.handlers;

import io.gridmc.angel.api.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class WorldArgumentHandler extends ArgumentHandler<World> {
    public WorldArgumentHandler() {
        setMessage("world_not_found", "The world \"%1\" was not found");

        addVariable("sender", "The command executor", new ArgumentVariable<World>() {
            @Override
            public World var(CommandSender sender, CommandArgument argument, String varName) throws CommandError {
                if (!(sender instanceof Player)) {
                    throw new CommandError("This is a player only command");
                }

                return ((Player) sender).getWorld();
            }
        });
    }

    @Override
    public World transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        World world = Bukkit.getWorld(value);
        if (world == null) {
            throw new TransformError("World '" + value + "' not found");
        }
        return world;
    }
}
