package me.zowpy.command.provider.container;

import lombok.Getter;
import me.zowpy.command.provider.impl.bukkit.PlayerProvider;
import me.zowpy.command.provider.impl.primitive.DoubleProvider;
import me.zowpy.command.provider.impl.primitive.FloatProvider;
import me.zowpy.command.provider.impl.primitive.IntegerProvider;
import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.impl.primitive.BooleanProvider;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/10/2022
 * Project: CommandAPI
 */

@Getter
public class ProviderContainer {

    private final ConcurrentMap<Class<?>, Provider<?>> providers = new ConcurrentHashMap<>();

    public ProviderContainer() {
        providers.put(Integer.class, new IntegerProvider());
        providers.put(int.class, new IntegerProvider());

        providers.put(Double.class, new DoubleProvider());
        providers.put(double.class, new DoubleProvider());

        providers.put(Boolean.class, new BooleanProvider());
        providers.put(boolean.class, new BooleanProvider());

        providers.put(Float.class, new FloatProvider());
        providers.put(float.class, new FloatProvider());

        providers.put(Player.class, new PlayerProvider());
    }
}
