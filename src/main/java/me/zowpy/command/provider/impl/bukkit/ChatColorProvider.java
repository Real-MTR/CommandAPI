package me.zowpy.command.provider.impl.bukkit;

import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExceptionType;
import me.zowpy.command.provider.exception.CommandExitException;
import me.zowpy.command.util.C;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @project CommandAPI is a property of MTR
 * @date 7/21/2024
 */

public class ChatColorProvider implements Provider<ChatColor> {

    @Override
    public ChatColor provide(String s) throws CommandExitException {
        try {
            return ChatColor.valueOf(s);
        } catch (Exception ignored) {
            throw new CommandExitException(C.colorize("&cInvalid Chat Color!"), CommandExceptionType.INVALID_ARGUMENT);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String supplied) {
        return Arrays.stream(ChatColor.values())
                .map(ChatColor::name)
                .filter(name -> name.toLowerCase().startsWith(supplied.toLowerCase()))
                .collect(Collectors.toList());
    }
}
