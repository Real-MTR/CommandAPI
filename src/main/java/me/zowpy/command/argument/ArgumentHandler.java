package me.zowpy.command.argument;

import lombok.RequiredArgsConstructor;
import me.zowpy.command.CommandAPI;
import me.zowpy.command.annotation.*;
import me.zowpy.command.command.LyraCommand;
import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.exception.CommandExitException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/8/2022
 * Project: CommandAPI
 */

@RequiredArgsConstructor
public class ArgumentHandler {

    private final CommandAPI commandAPI;

    public List<Argument> buildArguments(Method method) {
        List<Argument> arguments = new ArrayList<>();

        int i = 0;
        for (Parameter parameter : method.getParameters()) {
            /*if (method.getParameters().length > i + 1 && parameter.isAnnotationPresent(Optional.class)) {
                throw new IllegalArgumentException("The optional parameter has to be the last parameter.");
            }*/

            if (method.getParameters().length > i + 1 && parameter.isAnnotationPresent(Combined.class)) {
                throw new IllegalArgumentException("The optional parameter has to be the last parameter.");
            }

            Argument argument = new Argument(parameter.getType());

            if (parameter.isAnnotationPresent(Named.class)) {
                argument.setNamed(true);
                argument.setName(parameter.getAnnotation(Named.class).value());
            }

            if (parameter.isAnnotationPresent(Optional.class)) {
                argument.setOptional(true);
            }

            if (parameter.isAnnotationPresent(Combined.class)) {
                argument.setCombined(true);
            }

            if (parameter.isAnnotationPresent(Range.class)) {
                argument.setRange(true);

                Range range = parameter.getAnnotation(Range.class);

                argument.setMin(range.min());
                argument.setMax(range.max());
            }

            if (parameter.isAnnotationPresent(Flag.class)) {
                argument.setFlag(true);
                argument.setFlagString(parameter.getAnnotation(Flag.class).value());

                if (parameter.getType() != Boolean.class && parameter.getType() != boolean.class) {
                    throw new IllegalArgumentException("The flag parameter needs to be a boolean!");
                }
            }

            if (parameter.isAnnotationPresent(Sender.class)) {
                if (parameter.getType() == CommandSender.class || parameter.getType() == ConsoleCommandSender.class || parameter.getType() == Player.class) {
                    argument.setSender(true);
                }else {
                    throw new IllegalArgumentException("The sender parameter is not compatible with '" + parameter.getType().getSimpleName() + "'");
                }
            }


            arguments.add(argument);
            i++;
        }

        return arguments;
    }

    public List<Object> processArguments(CommandSender sender, String[] args, Method method, LyraCommand lyraCommand) throws CommandExitException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Object> toReturn = new ArrayList<>();

        List<Argument> requiredArgs = buildArguments(method);
        List<Argument> essentialArgs = requiredArgs.stream()
                .filter(argument -> !argument.isOptional() && !argument.isSender() && !argument.isFlag()).collect(Collectors.toList());

        boolean containsCombinedString = essentialArgs.stream().anyMatch(Argument::isCombined);

        int parameters = method.getParameterCount();

        // usage message
        if (!containsCombinedString && args.length < essentialArgs.size()) {
            throw new CommandExitException(ChatColor.RED + "Usage: " + generateUsage(lyraCommand, requiredArgs));
        }

        if (args.length < essentialArgs.size()) {
            throw new CommandExitException(ChatColor.RED + "Usage: " + generateUsage(lyraCommand, requiredArgs));
        }

        int indexed = 0;

       /* boolean found = true;

        if (args.length > 0) {
            for (String alias : lyraCommand.getAliases()) {
                String[] split = alias.split("\\s+");
                int size = split.length;

                int correct = 0;

                for (int i = 0; i < size; i++) {
                    int checkIndex = Math.max(0, i - 1);

                    if (args[checkIndex].equalsIgnoreCase(split[i])) {
                        correct++;

                        System.out.println("Correct: " + args[checkIndex]);
                    }
                }

                if (correct == size) {
                    //indexed = size - 1;

                    if (lyraCommand.getSubCommands().stream().anyMatch(command -> command.getArguments() == requiredArgs && command.getName().equalsIgnoreCase(alias) || Arrays.stream(command.getAliases()).anyMatch(s -> s.equalsIgnoreCase(alias)))) {
                        found = true;
                        System.out.println("it matched my nigga");
                        break;
                    }
                }else {
                    found = false;
                }
            }
        }

        if (!found) {
            sender.sendMessage(ChatColor.RED + "Usage: " +  generateUsage(lyraCommand, requiredArgs));
            return null;
        } */

        for (Argument argument : requiredArgs) {

            // flags
            if (argument.isFlag()) {
                if (args.length <= indexed) {
                    toReturn.add(false);
                    continue;
                }

                String flagArg = args[indexed];
                boolean flag = argument.getFlagString().equalsIgnoreCase(flagArg) || flagArg.equalsIgnoreCase("-" + argument.getFlagString());

                toReturn.add(flag);

                if (flag) {
                    indexed++;
                }

                continue;
            }

            // combined
            if ((parameters - indexed) >= 1 && argument.isCombined()) {
                if (argument.getType() != String.class) {
                    throw new IllegalArgumentException("The combined parameter needs to be a string");
                }

                StringBuilder builder = new StringBuilder();

                for (String s : Arrays.copyOfRange(args, indexed, args.length)) {
                    builder.append(s).append(" ");
                }

                toReturn.add(builder.toString().trim());
                break;
            }

            // optionals
            if (argument.isOptional()) {

                Object obj = null;

                try {
                    obj = provide(argument, args[indexed]);
                }catch (Exception exception) {
                    if (exception instanceof CommandExitException) {
                        throw exception;
                    }
                }

                if (obj == null || argument.getType().isAssignableFrom(obj.getClass()) || obj.getClass().isAssignableFrom(argument.getType())) {
                    toReturn.add(obj);
                }

                if (obj != null) {
                    indexed++;
                }

                continue;
            }
            /*if ((parameters - indexed) >= 1 && argument.isOptional()) {
                if ((args.length - indexed) >= 1) {
                    toReturn.add(provide(argument, args[indexed]));
                }else {
                    toReturn.add(null);
                }

                continue;
            } */

            // sender
            if (argument.isSender()) {
                toReturn.add(sender);
                continue;
            }

            toReturn.add(provide(argument, args[indexed]));

            indexed++;
        }

        return toReturn;
    }

    public Object provide(Argument argument, String arg) throws CommandExitException {
        Object object = null;

        if (argument.getType() == String.class) {
            object = arg;
        } else {
            Provider<?> provider = commandAPI.getProviderContainer().getProviders().get(argument.getType());

            if (provider == null) {
                throw new IllegalArgumentException("There is no supported provider for '" + argument.getType().getName() + "'");
            }

            object = provider.provide(arg);

            if ((argument.getType() == Integer.class || argument.getType() == int.class) && argument.isRange()) {
                int i = (int) object;

                if (argument.getMin() > i) {
                    throw new CommandExitException(ChatColor.RED + "The input was lower than the minimum value (" + argument.getMin() + ")");
                }

                if (argument.getMax() < i) {
                    throw new CommandExitException(ChatColor.RED + "The input was higher than the maximum value (" + argument.getMax() + ")");
                }
            }

        }

        return object;
    }

    public String generateUsage(LyraCommand lyraCommand, List<Argument> arguments) {
        StringBuilder builder = new StringBuilder();

        builder.append("/").append(lyraCommand.getName()).append(" ");

        int indexed = 0;

        for (Argument argument : arguments) {
            if (argument.isSender()) continue;

            if (argument.isOptional() || argument.isFlag()) {
                builder.append("[");
            }else {
                builder.append("<");
            }

            if (argument.isNamed()) {
                builder.append(argument.getName());
            } else if (argument.isFlag()) {
                builder.append("-").append(argument.getFlagString());
            } else {
                builder.append("arg").append(indexed);
            }

            if (argument.isOptional() || argument.isFlag()) {
                builder.append("]").append(" ");
            } else {
                builder.append(">").append(" ");
            }

            indexed++;
        }

        return builder.toString();
    }
}
