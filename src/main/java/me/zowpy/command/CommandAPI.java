package me.zowpy.command;

import lombok.Getter;
import lombok.Setter;
import me.zowpy.command.argument.ArgumentHandler;
import me.zowpy.command.container.CommandContainer;
import me.zowpy.command.format.MessageFormat;
import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.container.ProviderContainer;
import me.zowpy.command.registrar.CommandRegistrar;
import me.zowpy.command.registrar.RegistrarCompleter;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/8/2022
 * Project: CommandAPI
 */

@Getter @Setter
public class CommandAPI {

    private final Plugin plugin;

    private final ProviderContainer providerContainer;
    private final CommandContainer commandContainer;
    private final ArgumentHandler argumentHandler;

    private final CommandRegistrar commandRegistrar;
    private final RegistrarCompleter registrarCompleter;

    private final Executor executor;
    private MessageFormat messageFormat;

    public CommandAPI(Plugin plugin) {
        this.plugin = plugin;

        this.providerContainer = new ProviderContainer();
        this.commandContainer = new CommandContainer();
        this.argumentHandler = new ArgumentHandler(this);

        this.commandRegistrar = new CommandRegistrar(this);
        this.registrarCompleter = new RegistrarCompleter(this);

        this.executor = Executors.newFixedThreadPool(1);
        this.messageFormat = new MessageFormat();
    }

    public RegistrarCompleter beginCommandRegister() {
        return registrarCompleter;
    }

    public CommandAPI bind(Class<?> clazz, Provider<?> provider) {
        providerContainer.getProviders().put(clazz, provider);
        return this;
    }
}
