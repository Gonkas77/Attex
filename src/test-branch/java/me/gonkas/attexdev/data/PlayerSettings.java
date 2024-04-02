package me.gonkas.attexdev.data;

import me.gonkas.attexdev.Attex;
import me.gonkas.attexdev.chats.GroupChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static me.gonkas.attexdev.util.Strings.convertStringListToUUIDArrayList;

public class PlayerSettings {

    char chat_type; // 'l' == local; 't' == team; 'p' == party
    UUID last_pm; // player uuid
    String last_gc; // groupchat key
    ArrayList<String> groupchat_list; // array of gc keys
    ArrayList<String> groupchatinv_list; // array of gc keys for gcs player was invited to
    ArrayList<UUID> friend_list; // array of player uuid

    boolean[] changes = new boolean[6]; // minor optimization to only save changed settings and not all settings

    public PlayerSettings(Player player, YamlConfiguration config) {
        chat_type = config.getString("info.chats.type").charAt(0);
        last_pm = config.getObject("info.chats.last-pm", UUID.class);
        last_gc = config.getString("info.groupchats.last-gc");
        groupchat_list = (ArrayList<String>) config.getStringList("info.groupchats.list");
        groupchatinv_list = (ArrayList<String>) config.getStringList("info.groupchats.invites");
        friend_list = convertStringListToUUIDArrayList(config.getStringList("info.friends.list"));

        Attex.PLAYERGC.put(player, groupchat_list);
        Attex.PLAYERGCINVITES.put(player, groupchatinv_list);
    }

    public char getChatType() {return chat_type;}
    public UUID getLastPMTarget() {return last_pm;}
    public String getLastGCTargetKey() {return last_gc;}
    public ArrayList<String> getGroupChatList() {return groupchat_list;}
    public ArrayList<String> getGroupChatInviteList() {return groupchatinv_list;}
    public ArrayList<UUID> getFriendList() {return friend_list;}
    public boolean[] getChanges() {return changes;}

    public boolean setChatType(char c) {
        if (c == 'l' || c == 't' || c == 'p') {
            chat_type = c;
            changes[0] = true;
            return true;
        } else {return false;}
    }
    public boolean setPMTarget(String name) {
        Player target = Bukkit.getPlayerExact(name);
        if (target == null || !target.isOnline()) {return false;}
        last_pm = target.getUniqueId();
        changes[1] = true;
        return true;
    }
    public boolean setPMTarget(Player player) {
        if (player == null || !player.isOnline()) {return false;}
        last_pm = player.getUniqueId();
        changes[1] = true;
        return true;
    }
    public boolean setGCTarget(String key) {
        GroupChat group = GroupChat.getGroupChatWithKey(key);
        if (group.isGhost()) {return false;}
        else {
            last_gc = group.getKey();
            changes[2] = true;
        } return true;
    }
    public boolean setGCTarget(GroupChat gc) {
        if (gc.isGhost()) {return false;}
        else {
            last_gc = gc.getKey();
            changes[2] = true;
        } return true;
    }

    public void addGroupChat(String key) {
        groupchat_list.add(key);
        changes[3] = true;
    }
    public void addGroupChat(GroupChat gc) {
        groupchat_list.add(gc.getKey());
        changes[3] = true;
    }
    public void addFriend(String name) {
        friend_list.add(Bukkit.getPlayerExact(name).getUniqueId());
        changes[4] = true;
    }

    public boolean checkIfGCInList(String key) {return groupchat_list.contains(key);}
    public boolean checkIfGCInList(GroupChat gc) {return groupchat_list.contains(gc.getKey());}
    public boolean checkIfFriendInList(String name) {return friend_list.contains(Bukkit.getPlayerExact(name).getUniqueId());}
}