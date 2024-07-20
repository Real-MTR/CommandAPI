package me.zowpy.command.format;

import me.zowpy.command.argument.Argument;
import me.zowpy.command.command.LyraCommand;
import me.zowpy.command.util.C;

/**
 * @project LyraCommand.java is a property of MTR
 * @date 7/20/2024
 */

public class MessageFormat {

    public String noPermission(LyraCommand command) {
        return C.colorize("&cYou do not have permission to execute the command (/" + command.getName() + ")");
    }

    public String playersOnly(LyraCommand command) {
        return C.colorize("&cOnly players are allowed to execute this command!");
    }

    public String consoleOnly(LyraCommand command) {
        return C.colorize("&cOnly consoles are allowed to execute this command!");
    }

    public String registeredIncorrectly(LyraCommand command) {
        return C.colorize("&cError: This command was not registered correctly! Please contact an administrator.");
    }

    public String errorOccurred(LyraCommand command) {
        return C.colorize("&cAn error occurred while performing the command (/" + command.getName() + ")");
    }

    public String lowerThanMinimum(Argument argument) {
        return C.colorize("&cThe input was lower than the minimum value (" + argument.getMin() + ")");
    }

    public String higherThanMaximum(Argument argument) {
        return C.colorize("&cThe input was higher than the maximum value (" + argument.getMin() + ")");
    }
}
