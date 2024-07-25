package me.zowpy.command.util;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class HelpMessageBuilder {

    private final List<HelpComponent> lines;

    public HelpMessageBuilder addLine(String line, ClickEvent clickEvent, HoverEvent hoverEvent) {
        lines.add(new HelpComponent(line, true, true, clickEvent, hoverEvent));
        return this;
    }

    public HelpMessageBuilder addLine(String line, ClickEvent clickEvent) {
        lines.add(new HelpComponent(line, true, false, clickEvent, null));
        return this;
    }

    public HelpMessageBuilder addLine(String line, HoverEvent hoverEvent) {
        lines.add(new HelpComponent(line, false, true, null, hoverEvent));
        return this;
    }

    public HelpMessageBuilder addLine(String line) {
        lines.add(new HelpComponent(line, false, false, null, null));
        return this;
    }

    public void sendMessage(CommandSender sender) {
        if(sender instanceof Player) {
            Player player = (Player) sender;

            for (HelpComponent component : lines) {
                TextComponent message = new TextComponent(C.colorize(component.line));

                if (component.hoverable && component.hoverEvent != null) {
                    message.setHoverEvent(component.hoverEvent);
                }

                if (component.clickable && component.clickEvent != null) {
                    message.setClickEvent(component.clickEvent);
                }

                player.spigot().sendMessage(message);
            }
        }
    }

    @AllArgsConstructor
    static class HelpComponent {
        public final String line;
        public final boolean clickable, hoverable;
        public final ClickEvent clickEvent;
        public final HoverEvent hoverEvent;
    }
}