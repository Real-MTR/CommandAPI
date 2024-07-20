package me.zowpy.command.registrar;

import lombok.RequiredArgsConstructor;
import me.zowpy.command.CommandAPI;
import me.zowpy.command.command.LyraCommand;
import me.zowpy.command.util.ClassUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/30/2022
 * Project: CommandAPI
 */

@RequiredArgsConstructor
public class RegistrarCompleter {

    private final CommandAPI commandAPI;

    public RegistrarCompleter register(Object object) {
        return commandAPI.getCommandRegistrar().register(object);
    }

    public RegistrarCompleter register(String packageName) {
        for (Class<?> clazz : ClassUtil.getClassesInPackage(commandAPI.getPlugin(), packageName)) {
            register(clazz);
        }

        return this;
    }

    public CommandAPI endRegister() {
        List<LyraCommand> addCommands = new ArrayList<>();
        List<LyraCommand> removeCommands = new ArrayList<>();

        for (LyraCommand command : commandAPI.getCommandContainer().getCommands()
                .stream().sorted(Comparator.comparingInt(o -> o.getName().split("\\s+").length)).collect(Collectors.toList())) {
            String[] name = command.getName().split("\\s+");

            if (name.length == 1) {
                commandAPI.getCommandRegistrar().registerBukkit(command);
                continue;
            }

            LyraCommand parent = commandAPI.getCommandContainer().getCommand(name[0]);

            if (parent == null) {
                parent = new LyraCommand(name[0], new String[0], "", false, "");
            }

            parent.getSubCommands().add(command);

            commandAPI.getCommandRegistrar().registerBukkit(parent);

            LyraCommand finalParent = parent;

            if (addCommands.stream().anyMatch(lyraCommand -> lyraCommand.getName().equals(finalParent.getName()))) {
                addCommands.removeIf(lyraCommand -> lyraCommand.getName().equals(finalParent.getName()));
            }

            addCommands.add(parent);
            removeCommands.add(command);
        }

        commandAPI.getCommandContainer().getCommands().removeAll(removeCommands);
        commandAPI.getCommandContainer().getCommands().addAll(addCommands);

        return commandAPI;
    }
}
