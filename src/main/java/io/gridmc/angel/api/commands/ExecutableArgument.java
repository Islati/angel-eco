package io.gridmc.angel.api.commands;

import org.bukkit.command.CommandSender;

public interface ExecutableArgument {
    public Object execute(CommandSender sender, Arguments args) throws CommandError;
}
