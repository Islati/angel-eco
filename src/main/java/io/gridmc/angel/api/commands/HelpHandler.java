package io.gridmc.angel.api.commands;

public interface HelpHandler {
    public String[] getHelpMessage(RegisteredCommand command);

    public String getUsage(RegisteredCommand command);
}
