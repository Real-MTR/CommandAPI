package me.zowpy.command.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.ChatColor;

/**
 * @project CommandAPI is a property of MTR
 * @date 7/20/2024
 */

public class TextUtil {

    public static final String UNICODE_VERTICAL_BAR = StringEscapeUtils.unescapeJava("┃");
    public static final String UNICODE_CAUTION = StringEscapeUtils.unescapeJava("⚠");
    public static final String UNICODE_ARROW_LEFT = StringEscapeUtils.unescapeJava("◀");
    public static final String UNICODE_ARROW_RIGHT = StringEscapeUtils.unescapeJava("▶");
    public static final String UNICODE_ARROWS_LEFT = StringEscapeUtils.unescapeJava("«");
    public static final String UNICODE_ARROWS_RIGHT = StringEscapeUtils.unescapeJava("»");
    public static final String UNICODE_HEART = StringEscapeUtils.unescapeJava("❤");
    public static final String MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------";
    public static final String CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------";
    public static final String SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------";

    public static final char POINT = '●';
    public static final char HEART = '❤';
    public static final String LINE = "▏";
    public static final String LINE_2 = "▎";
    public static final String LINE_3 = "▍";
    public static final String LINE_4 = "▌";
    public static final String LINE_5 = "▋";
    public static final String LINE_6 = "▊";
    public static final String LINE_7 = "▉";
    public static final String LINE_8 = "█";
}
