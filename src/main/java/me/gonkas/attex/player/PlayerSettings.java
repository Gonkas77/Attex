package me.gonkas.attex.player;

import me.gonkas.attex.Attex;
import me.gonkas.attex.chats.GroupChat;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class PlayerSettings {

    private String chat;
    private String chat_target;
    private String group_chat_target;
    private List<GroupChat> group_chats;

    public boolean[] changes;

    public PlayerSettings(Player player) {
        File player_file = new File(Attex.PLAYERDATAFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(player_file);

        chat = config.getString("chat.selected");
        chat_target = config.getString("chat.target");
        group_chat_target = config.getString("chat.group.selected");
        for (String key : config.getStringList("chat.groups.get")) {
            group_chats.add(GroupChat.getGroupChatWithKey(key));
        }
    }

    public void setChatType(String s) {chat = s;}
    public void setChatTarget(String s) {chat_target = s;}
    public void setGroupChatTarget(GroupChat gc) {group_chat_target = gc.getKey();}

    public String getSelectedChat() {return chat;}
    public String getSelectedGroup() {return group_chat_target;}
    public String getChatTarget() {return chat_target;}
    public List<GroupChat> getGroupChats() {return group_chats;}
    public boolean[] getChanges() {return changes;}
}
