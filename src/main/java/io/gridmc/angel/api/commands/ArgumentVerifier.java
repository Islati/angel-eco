package io.gridmc.angel.api.commands;

import org.bukkit.command.CommandSender;

public interface ArgumentVerifier<T> {
    public void verify(CommandSender sender, CommandArgument argument, String verifyName, String[] verifyArgs, T value, String valueRaw) throws VerifyError;
}
