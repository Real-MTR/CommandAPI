package me.zowpy.command.provider.impl.bukkit;

import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExitException;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @project CommandAPI is a property of MTR
 * @date 7/21/2024
 */

public class GameModeProvider implements Provider<GameMode> {

    @Override
    public GameMode provide(String s) throws CommandExitException {
        switch (s.toLowerCase()) {
            case "creative":
            case "c":
            case "1":
                return GameMode.CREATIVE;
            case "adventure":
            case "a":
            case "2":
                return GameMode.ADVENTURE;
            case "survival":
            case "s":
            case "0":
                return GameMode.SURVIVAL;
            case "spectator":
            case "sp":
            case "3":
                return GameMode.SPECTATOR;
        }
        return null;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String supplied) {
        return Arrays.stream(GameMode.values()).map(GameMode::name).collect(Collectors.toList());
    }
}
