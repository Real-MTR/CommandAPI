package me.zowpy.command.provider.impl.bukkit;

import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExceptionType;
import me.zowpy.command.provider.exception.CommandExitException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

/**
 * @project CommandAPI is a property of MTR
 * @date 7/20/2024
 */

public class OfflinePlayerProvider implements Provider<OfflinePlayer> {

    @Override
    public OfflinePlayer provide(String s) throws CommandExitException {

        OfflinePlayer player = Bukkit.getOfflinePlayer(s);

        if (player == null) {
            throw new CommandExitException(ChatColor.RED + "That player does not exist!", CommandExceptionType.INVALID_ARGUMENT);
        }

        return player;
    }
}
