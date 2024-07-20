# CommandAPI
[![](https://jitpack.io/v/Real-MTR/CommandAPI.svg)](https://jitpack.io/#Real-MTR/CommandAPI)
****
**Credits: @Zowpy**

_A light-weight yet powerful CommandAPI created by Zowpy and maintained by MTR_

# Simple Commands Example

```java
public class BroadcastCommand {

    @Command(name = "broadcast")
    public void broadcast(@Sender ConsoleCommandSender sender, @Combined String text) {
        Bukkit.broadcastMessage(text);
    }
}
```

# Commands & Subcommands Example
```java
public class NiceCommand {
    
    @Command(name = "nice")
    public void idk(@Sender CommandSender sender) {
        sender.sendMessage("NICE");
    }

    @Command(name = "nice damn")
    public void idk2(@Sender CommandSender sender) {
        sender.sendMessage("NICE damnnnnn");
    }
}
```

# Registeration

```java
public class BroadcastPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandAPI commandAPI = new CommandAPI();
        commandAPI.beginCommandRegister()
                .register(new BroadcastCommand())
                .endRegister();
    }
}
```

# Providers

Every argument/parameter needs its own provider

There are built-in providers which are all the primitive types and the bukkit player

You can create your own provider

# Creating a Provider

```java
public class UUIDProvider implements Provider<UUID> {
    
    @Override
    public UUID provide(String s) throws CommandExitException {
        try {
            return UUID.fromString(s);
        } catch (Exception e) {
            throw new CommandExitException(ChatColor.RED + "Not a valid uuid");
        }
    }
}
```

The paramater in the provide method is the argument given

The message given in the CommandExitException's constructor is sent to the command sender when the exception is thrown

# How to get the dependency?
- Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependencies>
    <dependency>
        <groupId>com.github.Real-MTR</groupId>
        <artifactId>CommandAPI</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```
- Gradle
```groovy
repositories {
    mavenCentral()
    
    maven { 
        url 'https://jitpack.io'
    }
}

dependencies {
    implementation 'com.github.Real-MTR:CommandAPI:VERSION'
}
```