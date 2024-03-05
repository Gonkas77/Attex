package me.gonkas.attex;

import me.gonkas.attex.chats.GroupChat;
import me.gonkas.attex.commands.ChatSelector;
import me.gonkas.attex.commands.GroupChatCommand;
import me.gonkas.attex.player.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class Attex extends JavaPlugin {

    public static FileConfiguration CONFIG;
    public static ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

    public static File PLAYERDATAFOLDER = new File("plugins/Attex/player_data");
    public static HashMap<Player, PlayerSettings> PLAYERSETTINGS = new HashMap<>();
    public static HashMap<Player, ArrayList<GroupChat>> PLAYERGC = new HashMap<>();
    public static HashMap<Player, ArrayList<GroupChat>> PLAYERGCINVITES = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();
        CONFIG = getConfig();

        if (!PLAYERDATAFOLDER.exists()) {PLAYERDATAFOLDER.mkdirs();}

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        getCommand("chat").setExecutor(new ChatSelector());
        getCommand("groupchat").setExecutor(new GroupChatCommand());

        CONSOLE.sendMessage("");
        CONSOLE.sendMessage("§9[Attex]§a Plugin v0.1 initialized successfully!");
        CONSOLE.sendMessage("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void consoleInfo(String s) {CONSOLE.sendMessage("§9[Attex/INFO] §b" + s);}
    public static void consoleWarn(String s) {CONSOLE.sendMessage("§9[Attex/WARN] §e" + s);}

    public static void playerSendInfo(Player player, String s) {player.sendMessage("§8[§bAttex§8] §f" + s);}
    public static void playerSendWarn(Player player, String s) {player.sendMessage("§8[§bAttex§8] §c" + s);}

    public static void setDefaultSettings(YamlConfiguration config) {
        config.addDefault("chat.selected", "local");
        config.addDefault("chat.groups.get", Arrays.stream(new String[0]).toList());
        config.addDefault("chat.groups.invites", Arrays.stream(new String[0]).toList());
    }
}