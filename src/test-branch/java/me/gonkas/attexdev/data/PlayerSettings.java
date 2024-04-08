package me.gonkas.attexdev.data;

import me.gonkas.attexdev.Attex;
import me.gonkas.attexdev.chats.GroupChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static me.gonkas.attexdev.util.Strings.convertKeyListToGCNameList;
import static me.gonkas.attexdev.util.Strings.convertStringListToUUIDArrayList;

public class PlayerSettings {

    char chat_type; // 'l' == local; 't' == team; 'p' == party; 'g' == group
    UUID last_pm; // player uuid
    String last_gc; // groupchat key
    ArrayList<String> groupchat_list; // array of gc keys
    ArrayList<String> groupchatinv_list; // array of gc keys for gcs player was invited to
    ArrayList<UUID> friend_list; // array of player uuid

    boolean[] changes = new boolean[6]; // minor optimization to only save changed settings and not all settings

    public PlayerSettings(YamlConfiguration config) {
        chat_type = config.getString("info.chats.type").charAt(0);
        last_pm = config.getObject("info.chats.last-pm", UUID.class);
        last_gc = config.getString("info.groupchats.last-gc");
        groupchat_list = (ArrayList<String>) config.getStringList("info.groupchats.list");
        groupchatinv_list = (ArrayList<String>) config.getStringList("info.groupchats.invites");
        friend_list = convertStringListToUUIDArrayList(config.getStringList("info.friends.list"));

        DataManagement.loadGroupChatFile(last_gc);
    }

    public char getChatType() {return chat_type;}
    public UUID getLastPMTarget() {return last_pm;}
    public String getLastGCTargetKey() {return last_gc;}
    public ArrayList<String> getGroupChatKeyList() {return groupchat_list;}
    public ArrayList<String> getGroupChatInvitesKeyList() {return groupchatinv_list;}
    public ArrayList<UUID> getFriendsUUIDList() {return friend_list;}
    public boolean[] getChanges() {return changes;}

    public String getGroupChatNamesToString() {
        StringBuilder list = new StringBuilder();
        ArrayList<String> names = convertKeyListToGCNameList(groupchat_list);
        for (int i=0; i < names.size() - 1; i++) {list.append("§b").append(names.get(i)).append("§f, ");}
        list.deleteCharAt(list.length() - 2).append("and ").append("§b").append(names.get(names.size() - 1)).append("§f.");
        return list.toString();
    }

    public boolean setChatType(char c) {
        if (c == 'l' || c == 't' || c == 'p' || c == 'g') {
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
    public boolean setGCTarget(String key) throws IOException {

        // unloading previous group chat if no other gc member is currently using it
        GroupChat gc = GroupChat.getGroupChatWithKey(last_gc);
        boolean unload = true;
        for (Player p : gc.getPlayers()) {
            if (Attex.PLAYERSETTINGS.get(p).getLastGCTargetKey().equals(last_gc)) {unload = false; break;}
        }
        if (unload) {GroupChat.unloadGroupChat(last_gc);}

        // actually setting the gc target
        GroupChat group = GroupChat.getGroupChatWithKey(key);
        if (group.isGhost()) {return false;}
        else {
            DataManagement.loadGroupChatFile(key);
            last_gc = group.getKey();
            changes[2] = true;
        } return true;
    }
    public boolean setGCTarget(GroupChat gc) throws IOException {

        // unloading previous group chat if no other gc member is currently using it
        GroupChat group = GroupChat.getGroupChatWithKey(last_gc);
        boolean unload = true;
        for (Player p : group.getPlayers()) {
            if (Attex.PLAYERSETTINGS.get(p).getLastGCTargetKey().equals(last_gc)) {unload = false; break;}
        }
        if (unload) {GroupChat.unloadGroupChat(last_gc);}

        // actually setting the gc target
        if (gc.isGhost()) {return false;}
        else {
            DataManagement.loadGroupChatFile(gc.getKey());
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