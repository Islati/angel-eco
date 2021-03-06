package io.gridmc.angel.api.commands.handlers;

import io.gridmc.angel.api.commands.ArgumentHandler;
import io.gridmc.angel.api.commands.CommandArgument;
import io.gridmc.angel.api.commands.TransformError;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;


public class SoundArgumentHandler extends ArgumentHandler<Sound> {
    public SoundArgumentHandler() {
        setMessage("parse_error", "There is no sound named %1");
        setMessage("include_error", "There is no sound named %1");
        setMessage("exclude_error", "There is no sound named %1");
    }

    @Override
    public Sound transform(CommandSender sender, CommandArgument argument, String value) throws TransformError {
        return Sound.valueOf(value.toUpperCase());
    }


}
