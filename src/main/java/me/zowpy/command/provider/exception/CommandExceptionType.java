package me.zowpy.command.provider.exception;

/**
 * @project CommandAPI is a property of MTR
 * @date 7/20/2024
 */

public enum CommandExceptionType {
    INVALID_ARGUMENT,
    USAGE,
    LOWER_THAN_MINIMUM,
    HIGHER_THAN_MAXIMUM,
    PERMISSION,
    UNREGISTERED,
    CONSOLE_ONLY,
    PLAYERS_ONLY;
}
