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
}
