package me.zowpy.command.format;

import me.zowpy.command.command.LyraCommand;

/**
 * @project LyraCommand.java is a property of MTR
 * @date 7/20/2024
 */

public class MessageFormat {

    public String noPermission(LyraCommand command) {
        return "&cYou do not have permission to execute the command (/" + command.getName() + ")";
    }

    public String playersOnly(LyraCommand command) {
        return "&cOnly players are allowed to execute this command!";
    }

    public String consoleOnly(LyraCommand command) {
        return "&cOnly consoles are allowed to execute this command!";
    }

    public String registeredIncorrectly(LyraCommand command) {
        return "&cError: This command was not registered correctly! Please contact an administrator.";
    }

    public String errorOccurred(LyraCommand command) {
        return "&cAn error occurred while performing the command (/" + command.getName() + ")";
    }
}
