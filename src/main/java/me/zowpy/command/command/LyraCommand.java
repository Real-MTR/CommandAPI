package me.zowpy.command.command;

import lombok.Getter;
import lombok.Setter;
import me.zowpy.command.argument.Argument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public int getMatchProbability(CommandSender sender, String label, String[] args, boolean tabbed) {
        AtomicInteger probability = new AtomicInteger(0);
        List<String> names = new ArrayList<>(Arrays.asList(aliases));
        names.add(name);

        names.forEach(name -> {
            StringBuilder nameLabel = new StringBuilder(label).append(" ");
            String[] splitName = name.split(" ");
            int nameLength = splitName.length;

            for(int i = 1; i < nameLength; i++)
                if(args.length>= i) nameLabel.append(args[i - 1]).append(" ");

            if(name.equalsIgnoreCase(nameLabel.toString().trim())) {
                int requiredParameters = (int) this.getArguments().stream()
                        .filter(argument -> !argument.isOptional())
                        .count();

                int actualLength = args.length - (nameLength - 1);

                if(requiredParameters == actualLength || getArguments().size() == actualLength) {
                    probability.addAndGet(125);
                    return;
                }

                if(this.getArguments().size() > 0) {
                    Argument lastArgument = this.getArguments().get(this.getArguments().size() - 1);
                    if (lastArgument.isCombined() && actualLength > requiredParameters) {
                        probability.addAndGet(125);
                        return;
                    }
                }

                if(!tabbed || splitName.length > 1 || getArguments().size() > 0)
                    probability.addAndGet(50);

                if(actualLength > requiredParameters)
                    probability.addAndGet(15);

                if(sender instanceof Player && consoleOnly)
                    probability.addAndGet(-15);

                if(!(sender instanceof Player) && playerOnly)
                    probability.addAndGet(-15);

                if(!permission.equals("") && !sender.hasPermission(permission))
                    probability.addAndGet(-15);

                return;
            }

            String[] labelSplit = nameLabel.toString().split(" ");
            for(int i = 0; i < nameLength && i < labelSplit.length; i++)
                if(splitName[i].equalsIgnoreCase(labelSplit[i]))
                    probability.addAndGet(5);
        });

        return probability.get();
    }
}
