package me.zowpy.command.provider.impl.primitive;

import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExceptionType;
import me.zowpy.command.provider.exception.CommandExitException;
import org.bukkit.ChatColor;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/30/2022
 * Project: CommandAPI
 */
public class FloatProvider implements Provider<Float> {

    @Override
    public Float provide(String s) throws CommandExitException {
        try {
            return Float.parseFloat(s);
        }catch (Exception e) {
            throw new CommandExitException(ChatColor.RED + "Not a valid float!", CommandExceptionType.INVALID_ARGUMENT);
        }
    }
}
