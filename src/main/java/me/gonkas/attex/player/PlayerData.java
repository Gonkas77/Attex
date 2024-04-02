package me.gonkas.attex.player;

import me.gonkas.attex.Attex;
import me.gonkas.attex.chats.GroupChat;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerData {

    public static void createFile(Player player) throws IOException {
        File player_file = new File(Attex.PLAYERDATAFOLDER, player.getUniqueId() + ".yml");
        if (!player_file.exists()) {
            player_file.createNewFile();
            setDefaults(player_file);
        }
    }

    public static void setDefaults(File file) throws IOException {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("chat.selected", "local");
        config.set("chat.groups.selected", "");
        config.set("chat.groups.get", new ArrayList<String>(0));
        config.set("chat.groups.invites", new ArrayList<String>(0));
        config.save(file);
    }

    public static void loadFile(Player player) {
        File player_file = new File(Attex.PLAYERDATAFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(player_file);

        Attex.PLAYERSETTINGS.put(player, new PlayerSettings(player));
        Attex.PLAYERGCINVITES.put(player, new ArrayList<GroupChat>(0));
        for (String s : config.getStringList("chat.groups.invites")) {
            Attex.PLAYERGC.get(player).add(GroupChat.getGroupChat(player, s));
        }
        Attex.PLAYERGC.put(player, new ArrayList<GroupChat>(0));
        for (String s : config.getStringList("chat.groups.get")) {
            Attex.PLAYERGC.get(player).add(GroupChat.getGroupChat(player, s));
        }
    }

    public static void saveData(Player player) throws IOException {
        File file = new File(Attex.PLAYERDATAFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        PlayerSettings settings = Attex.PLAYERSETTINGS.get(player);
        for (int i=0; i < settings.getChanges().length; i++) {
            if (settings.getChanges()[i]) {
                switch (i) {
                    case 0: config.set("chat.selected", settings.getSelectedChat());
                    case 1: config.set("chat.target", settings.getChatTarget());
                    case 2: config.set("chat.group.selected", settings.getSelectedGroup());
                    case 3: config.set("chat.groups.get", settings.getGroupChats());
                }
            }
        } config.save(file);
    }
}
