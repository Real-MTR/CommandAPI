package me.zowpy.command.executor;

import lombok.Getter;
import lombok.SneakyThrows;
import me.zowpy.command.CommandAPI;
import me.zowpy.command.argument.Argument;
import me.zowpy.command.command.LyraCommand;
import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExceptionType;
import me.zowpy.command.provider.exception.CommandExitException;
import me.zowpy.command.util.C;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/17/2022
 * Project: CommandAPI
 */

@Getter
public class CommandExecutor extends BukkitCommand {

    private final CommandAPI commandAPI;
    private final LyraCommand lyraCommand;

    public CommandExecutor(CommandAPI commandAPI, LyraCommand command) {
        super(command.getName().split("\\s+")[0]);

        this.commandAPI = commandAPI;
        this.lyraCommand = command;

        this.description = command.getDescription();

        this.setAliases(Arrays.asList(command.getAliases()));
    }

    /**
     * this is for registering the aliases
     *
     * @param commandAPI commandAPI
     * @param command command
     * @param name name
     */

    public CommandExecutor(CommandAPI commandAPI, LyraCommand command, String name) {
        super(name.split("\\s+")[0]);

        this.commandAPI = commandAPI;
        this.lyraCommand = command;

        this.description = command.getDescription();

        this.setAliases(Arrays.asList(command.getAliases()));
    }

    @SneakyThrows
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        LyraCommand lyraCommand1 = lyraCommand;
        LyraCommand subcommand = lyraCommand.findSubCommand(args);

        if (subcommand != null) {
            lyraCommand1 = subcommand;
        }

        if (lyraCommand1.isPlayerOnly() && !(sender instanceof Player)) {
            C.sendMessage(sender, commandAPI.getMessageFormat().playersOnly(lyraCommand1));
            return true;
        }

        if (lyraCommand1.isConsoleOnly() && !(sender instanceof ConsoleCommandSender)) {
            C.sendMessage(sender, commandAPI.getMessageFormat().consoleOnly(lyraCommand1));
            return true;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!lyraCommand1.getPermission().isEmpty()) {
                if (!player.hasPermission(lyraCommand1.getPermission())) {
                    throw new CommandExitException(commandAPI.getMessageFormat().noPermission(lyraCommand1), CommandExceptionType.PERMISSION);
                }
            }
        }

        if (lyraCommand1.getMethod() == null || lyraCommand1.getCommandClass() == null) {
            throw new CommandExitException(commandAPI.getMessageFormat().registeredIncorrectly(lyraCommand1), CommandExceptionType.UNREGISTERED);
        }

        if (lyraCommand1.isAsync()) {
            LyraCommand finalLyraCommand = lyraCommand1;
            commandAPI.getExecutor().execute(() -> {
                try {
                    String[] commandNameArgs = finalLyraCommand.getName().split("\\s+");
                    List<Object> arguments = commandAPI.getArgumentHandler().processArguments(sender, commandNameArgs.length == 1 ? args : Arrays.copyOfRange(args, commandNameArgs.length - 1, args.length), finalLyraCommand.getMethod(), finalLyraCommand);

                    if (arguments != null) {
                        finalLyraCommand.getMethod().invoke(finalLyraCommand.getCommandClass(), arguments.toArray());
                    }

                } catch (CommandExitException e) {
                    sender.sendMessage(e.getExitMessage());
                } catch (Exception throwable) {
                    C.sendMessage(sender, commandAPI.getMessageFormat().errorOccurred(finalLyraCommand));
                    throwable.printStackTrace();
                }
            });
        } else {
            try {
                String[] commandNameArgs = lyraCommand1.getName().split("\\s+");
                List<Object> arguments = commandAPI.getArgumentHandler().processArguments(sender, commandNameArgs.length == 1 ? args : Arrays.copyOfRange(args, commandNameArgs.length - 1, args.length), lyraCommand1.getMethod(), lyraCommand1);

                if (arguments != null) {
                    lyraCommand1.getMethod().invoke(lyraCommand1.getCommandClass(), arguments.toArray());
                }

            } catch (CommandExitException e) {
                sender.sendMessage(e.getExitMessage());
            } catch (Exception e) {
                C.sendMessage(sender, commandAPI.getMessageFormat().playersOnly(lyraCommand1));
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
        try {
            List<LyraCommand> sortedNodes = commandAPI.getCommandContainer().getCommands().stream()
                    .sorted(Comparator.comparingInt(node -> node.getMatchProbability(sender, label, args, true)))
                    .collect(Collectors.toList());

            LyraCommand node = sortedNodes.get(sortedNodes.size() - 1);
            if(node.getMatchProbability(sender, label, args, true) >= 50) {

                int extraLength = node.getName().split(" ").length - 1;
                int arg = (args.length - extraLength) - 1;

                if(arg < 0 || node.getArguments().size() < arg + 1)
                    return new ArrayList<>();

                Argument argumentNode = node.getArguments().get(arg);
                return getTabComplete(argumentNode, sender, args[args.length - 1]);
            }

            List<String> names = new ArrayList<>(Arrays.asList(lyraCommand.getAliases()));
            names.add(lyraCommand.getName());

            return sortedNodes.stream()
                    .filter(sortedNode -> sortedNode.getPermission().isEmpty() || sender.hasPermission(sortedNode.getPermission()))
                    .map(sortedNode -> names.stream()
                            .map(name -> name.split(" "))
                            .filter(splitName -> splitName[0].equalsIgnoreCase(label))
                            .filter(splitName -> splitName.length > args.length)
                            .map(splitName -> splitName[args.length])
                            .collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .filter(name -> name.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                    .collect(Collectors.toList());
        } catch(Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String> getTabComplete(Argument node, CommandSender sender, String supplied) {
        Provider<?> processor = commandAPI.getProviderContainer().getProviderByType(node.getType());
        if(processor == null) return new ArrayList<>();

        return processor.tabComplete(sender, supplied);
    }
}
