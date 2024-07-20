package me.zowpy.command.provider.container;

import lombok.Getter;
import me.zowpy.command.provider.Provider;
import me.zowpy.command.provider.impl.bukkit.ChatColorProvider;
import me.zowpy.command.provider.impl.bukkit.GameModeProvider;
import me.zowpy.command.provider.impl.bukkit.OfflinePlayerProvider;
import me.zowpy.command.provider.impl.bukkit.PlayerProvider;
import me.zowpy.command.provider.impl.primitive.BooleanProvider;
import me.zowpy.command.provider.impl.primitive.DoubleProvider;
import me.zowpy.command.provider.impl.primitive.FloatProvider;
import me.zowpy.command.provider.impl.primitive.IntegerProvider;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
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
        providers.put(OfflinePlayer.class, new OfflinePlayerProvider());

        providers.put(ChatColor.class, new ChatColorProvider());
        providers.put(GameMode.class, new GameModeProvider());
    }

    public Provider<?> getProviderByType(Class<?> clazz) {
        return providers.get(clazz);
    }

    public void registerProvider(Class<?> clazz, Provider<?> provider) {
        providers.put(clazz, provider);
    }
}
