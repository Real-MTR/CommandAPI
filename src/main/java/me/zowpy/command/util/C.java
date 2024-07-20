package me.zowpy.command.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * @project LyraCommand.java is a property of MTR
 * @date 7/20/2024
 */

@UtilityClass
public class C {

    public String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public void sendMessage(CommandSender sender, String str) {
        sender.sendMessage(colorize(str));
    }
}
