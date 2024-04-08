package me.gonkas.attexdev;

import me.gonkas.attexdev.chats.GroupChat;
import me.gonkas.attexdev.commands.GCCommand;
import me.gonkas.attexdev.commands.GroupChatCommand;
import me.gonkas.attexdev.data.DataManagement;
import me.gonkas.attexdev.data.PlayerSettings;
import me.gonkas.attexdev.data.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public final class Attex extends JavaPlugin {

    public static FileConfiguration CONFIG;
    public static ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();

    public static File PLAYERSETTINGSFOLDER = new File("plugins/Attex/data/player_settings");
    public static File PLAYERSTATSFOLDER = new File("plugins/Attex/data/player_stats");
    public static File GROUPCHATSFOLDER = new File("plugins/Attex/data/groupchats");

    public static HashMap<Player, PlayerSettings> PLAYERSETTINGS = new HashMap<>();
    public static HashMap<Player, PlayerStats> PLAYERSTATS = new HashMap<>();
    public static HashMap<String, Short> GROUPCHATCODES = new HashMap<>();

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();
        CONFIG = getConfig();

        if (!PLAYERSETTINGSFOLDER.exists()) {PLAYERSETTINGSFOLDER.mkdirs();}
        if (!PLAYERSTATSFOLDER.exists()) {PLAYERSTATSFOLDER.mkdirs();}
        if (!GROUPCHATSFOLDER.exists()) {GROUPCHATSFOLDER.mkdirs();}
        antiReload();

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        getCommand("groupchat").setExecutor(new GroupChatCommand());
        getCommand("gc").setExecutor(new GCCommand());

        CONSOLE.sendMessage("");
        CONSOLE.sendMessage("§9[Attex]§a Plugin v0.1-beta initialized successfully!");
        CONSOLE.sendMessage("");
        
        // deletes all gc codes that have existed for 5 mins or more
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Short[] codes = GROUPCHATCODES.values().toArray(new Short[0]);
            for (int i=0; i < codes.length; i++) {
                codes[i]++;
                if (codes[i] >= 300) {
                    String code = GROUPCHATCODES.keySet().toArray(new String[0])[i];
                    GROUPCHATCODES.remove(code);
                    GroupChat.deleteCode(code);
                }
            }
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void antiReload() {
        for (Player p : Bukkit.getOnlinePlayers()) {DataManagement.loadPlayerSettingsFile(p);}
    }
}