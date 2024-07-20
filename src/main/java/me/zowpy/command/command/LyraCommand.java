package me.zowpy.command.command;

import lombok.Getter;
import lombok.Setter;
import me.zowpy.command.argument.Argument;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/17/2022
 * Project: CommandAPI
 */

@Getter @Setter
public class LyraCommand {

    private String name;
    private String[] aliases;
    private String description;

    private boolean async;
    private boolean playerOnly, consoleOnly;

    private Object commandClass;

    private final List<Argument> arguments = new ArrayList<>();
    private final List<LyraCommand> subCommands = new ArrayList<>();

    private String permission;

    private Method method;

    public LyraCommand(String name, String[] aliases, String description, boolean async, String permission) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.async = async;
        this.permission = permission;
    }

    public LyraCommand findSubCommand(String[] args) {
        for (LyraCommand command : subCommands) {
            String[] commandSplit = command.getName().split("\\s+");
            List<String> commandName = Arrays.asList(commandSplit).subList(1, commandSplit.length);

           // String name = Joiner.on(" ").join(commandName);
           // String joinedArgs = Joiner.on(" ").join(args);

            int nameCount = commandName.size();

            if (args.length < nameCount || args.length <= 0) {
                continue;
            }

            int indexed = 0;
            int correct = 0;

            for (int i = 0; i < nameCount; i++) {
                if (commandName.get(i).equalsIgnoreCase(args[i])) {
                    correct++;
                }

                indexed++;
            }

            if (indexed == correct) {
                return command;
            } else {
                for (String alias : command.getAliases()) {
                    String[] aliasSplit = alias.split("\\s+");
                    List<String> aliasCommandName = Arrays.asList(aliasSplit).subList(1, aliasSplit.length);

                    int aliasNameCount = aliasCommandName.size();

                    if (args.length < aliasNameCount) {
                        continue;
                    }

                    int aliasIndexed = 0;
                    int aliasCorrect = 0;

                    for (int i = 0; i < aliasNameCount; i++) {
                        if (aliasCommandName.get(i).equalsIgnoreCase(args[i])) {
                            aliasCorrect++;
                        }

                        aliasIndexed++;
                    }

                    if (aliasIndexed == aliasCorrect) {
                        return command;
                    }
                }
            }
        }

        return this;
    }
}
