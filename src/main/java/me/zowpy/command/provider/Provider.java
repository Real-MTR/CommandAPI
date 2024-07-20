package me.zowpy.command.provider;

import me.zowpy.command.provider.exception.CommandExitException;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/10/2022
 * Project: CommandAPI
 */
public interface Provider<T> {

    T provide(String s) throws CommandExitException;

    default List<String> tabComplete(CommandSender sender, String supplied) {
        return new ArrayList<>();
    }
}
