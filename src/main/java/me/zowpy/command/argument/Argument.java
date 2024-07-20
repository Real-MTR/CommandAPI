package me.zowpy.command.argument;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This Project is property of Zowpy © 2022
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 6/8/2022
 * Project: CommandAPI
 */

@Getter @Setter @RequiredArgsConstructor
public class Argument {

    private boolean named, optional, combined, sender, flag, range;
    private String name = "arg";

    private int min, max;

    private String flagString;

    private final Class<?> type;
}
