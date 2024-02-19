package me.gonkas.attex.player;

import me.gonkas.attex.Attex;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class PlayerSettings {

    public String chat;
    public String chat_target;
    public List<String> group_chats;

    public PlayerSettings(Player player) {
        File player_file = new File(Attex.PLAYERDATAFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(player_file);

        chat = config.getString("chat.selected");
        chat_target = config.getString("chat.target");
        group_chats = config.getStringList("chat.groups");
    }

    public String getSelectedChat() {return chat;}
    public List<String> getGroupChats() {return group_chats;}
}
