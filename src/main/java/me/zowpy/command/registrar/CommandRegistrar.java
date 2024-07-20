package me.zowpy.command.registrar;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.zowpy.command.CommandAPI;
import me.zowpy.command.annotation.Command;
import me.zowpy.command.annotation.Permission;
import me.zowpy.command.argument.Argument;
import me.zowpy.command.command.LyraCommand;
import me.zowpy.command.executor.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/17/2022
 * Project: CommandAPI
 */

@RequiredArgsConstructor
public class CommandRegistrar {

    private final CommandAPI commandAPI;

    public RegistrarCompleter register(Object object) {
        List<LyraCommand> add = new ArrayList<>();

        injectPlugin(object);

        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class)) continue;

            Command command = method.getAnnotation(Command.class);

            boolean hasPermission = method.isAnnotationPresent(Permission.class);
            String permission = hasPermission ? method.getAnnotation(Permission.class).value() : "";

            LyraCommand lyraCommand = new LyraCommand(command.name(), command.aliases(), command.description(), command.async(), permission);
            lyraCommand.setMethod(method);

            lyraCommand.setCommandClass(object);

            List<Argument> arguments = commandAPI.getArgumentHandler().buildArguments(method);
            lyraCommand.getArguments().addAll(arguments);

            Optional<Argument> argument = arguments.stream().filter(Argument::isSender).findFirst();

            if (argument.isPresent()) {
                if (argument.get().getType() == Player.class) {
                    lyraCommand.setPlayerOnly(true);
                }else if (argument.get().getType() == ConsoleCommandSender.class) {
                    lyraCommand.setConsoleOnly(true);
                }
            }

            add.add(lyraCommand);
        }

        add.removeIf(command -> {
            String[] name = command.getName().split("\\s+");

            if (name.length == 1) {
                return false;
            }

            LyraCommand parent = add.stream().filter(lyraCommand -> lyraCommand.getName().equalsIgnoreCase(name[0]))
                    .findFirst().orElse(null);

            if (parent == null) {
                return false;
            }

            parent.getSubCommands().add(command);

            return true;
        });

        commandAPI.getCommandContainer().getCommands().addAll(add);

        return commandAPI.getRegistrarCompleter();
    }

    @SneakyThrows
    private void injectPlugin(Object o) {
        Field field = Arrays.stream(o.getClass().getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(JavaPlugin.class) || JavaPlugin.class.isAssignableFrom(f.getType()))
                .findFirst().orElse(null);

        if (field == null) return;

        field.setAccessible(true);
        field.set(o, commandAPI.getPlugin());
        field.setAccessible(false);
    }

    public void registerBukkit(LyraCommand lyraCommand) {
        try {
            SimpleCommandMap commandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
            unregister(lyraCommand.getName(), commandMap);

            commandMap.register(commandAPI.getPlugin().getName().toLowerCase(), new CommandExecutor(commandAPI, lyraCommand));
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    private void unregister(String name, SimpleCommandMap commandMap) {
        Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);

        Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) knownCommandsField.get(commandMap);

        org.bukkit.command.Command command = knownCommands.get(name);

        if (command == null) return;

        command.unregister(commandMap);
        knownCommands.remove(name);

        knownCommandsField.set(commandMap, knownCommands);
    }
}
