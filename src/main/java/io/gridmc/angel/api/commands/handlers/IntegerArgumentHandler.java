package io.gridmc.angel.api.commands.handlers;

import io.gridmc.angel.api.commands.CommandArgument;
import io.gridmc.angel.api.commands.TransformError;
import org.bukkit.command.CommandSender;

public class IntegerArgumentHandler extends NumberArgumentHandler<Integer> {
    public IntegerArgumentHandler() {
        setMessage("parse_error", "The parameter [%p] is not an integer");
    }

    @Override
    public Integer transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new TransformError(argument.getMessage("parse_error"));
        }
    }
}
