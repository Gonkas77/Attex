package me.gonkas.attexdev;

import me.gonkas.attexdev.data.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public final class Attex extends JavaPlugin {

    public static FileConfiguration CONFIG;
    public static ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

    public static File PLAYERSETTINGSFOLDER = new File("plugins/Attex/data/player_settings");
    public static File PLAYERSTATSFOLDER = new File("plugins/Attex/data/player_statistics");
    public static File GROUPCHATSFOLDER = new File("plugins/Attex/data/groupchats");

    public static HashMap<Player, PlayerSettings> PLAYERSETTINGS = new HashMap<>();
    public static HashMap<Player, ArrayList<String>> PLAYERGC = new HashMap<>();
    public static HashMap<Player, ArrayList<String>> PLAYERGCINVITES = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();
        CONFIG = getConfig();

        if (!PLAYERSETTINGSFOLDER.exists()) {
            PLAYERSETTINGSFOLDER.mkdirs();}

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        CONSOLE.sendMessage("");
        CONSOLE.sendMessage("§9[Attex]§a Plugin v0.1-beta initialized successfully!");
        CONSOLE.sendMessage("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}