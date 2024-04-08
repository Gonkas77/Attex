package me.gonkas.attexdev.data;

import me.gonkas.attexdev.Attex;
import me.gonkas.attexdev.chats.GroupChat;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManagement {

    private static ArrayList<String> LOADEDGROUPCHATS = new ArrayList<>(0);

    public static void createPlayerSettingsFile(Player player) throws IOException {
        File data_file = new File(Attex.PLAYERSETTINGSFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);

        config.set("last-username", player.getName());
        config.set("chats.type", "l"); // 'l' == local; 't' == team; 'p' == party; 'g' == group
        config.set("chats.last-pm", player.getUniqueId().toString());
        config.set("groupchats.last-gc", "");
        config.set("groupchats.list", new ArrayList<String>(0));
        config.set("groupchats.invites", new ArrayList<String>(0));
        config.set("friends.list", Arrays.stream(new String[]{player.getUniqueId().toString()}).toList()); // delete this later on !!!!!!!!!!!!!!!!!!
        config.save(data_file);
    }

    public static void loadPlayerSettingsFile(Player player) {
        File data_file = new File(Attex.PLAYERSETTINGSFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);
        Attex.PLAYERSETTINGS.put(player, new PlayerSettings(config));
    }

    public static void savePlayerSettingsFile(Player player) throws IOException {
        File data_file = new File(Attex.PLAYERSETTINGSFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);

        if (!player.getName().equals(config.getString("last-username"))) {config.set("last-username", player.getName());}

        PlayerSettings settings = Attex.PLAYERSETTINGS.get(player);
        for (int i=0; i < settings.getChanges().length; i++) {
            if (settings.getChanges()[i]) {
                switch (i) {
                    case 0 -> config.set("chats.type", settings.getChatType());
                    case 1 -> config.set("chats.last-pm", settings.getLastPMTarget());
                    case 2 -> config.set("groupchats.last-gc", settings.getLastGCTargetKey());
                    case 3 -> config.set("groupchats.list", settings.getGroupChatKeyList());
                    case 4 -> config.set("groupchats.invites", settings.getGroupChatInvitesKeyList());
                    case 5 -> config.set("friends.list", settings.getFriendsUUIDList());
                }
            }
        } config.save(data_file);
    }

    public static void createPlayerStatsFile(Player player) throws IOException {
        File data_file = new File(Attex.PLAYERSTATSFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);

        config.set("kills", new ArrayList<>(List.of(0)));
        config.set("deaths", new ArrayList<>(List.of(0)));
        config.set("kills", new ArrayList<>(List.of(0)));
        config.set("kills", new ArrayList<>(List.of(0)));
        config.set("kills", new ArrayList<>(List.of(0)));
        config.set("kills", new ArrayList<>(List.of(0)));
        config.set("kills", new ArrayList<>(List.of(0)));
        config.set("kills", new ArrayList<>(List.of(0)));

        config.save(data_file);
    }

    public static void createGroupChatFile(GroupChat gc) throws IOException {
        File data_file = new File(Attex.GROUPCHATSFOLDER, gc.getKey() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);

        config.set("name", gc.getName());
        config.set("key", gc.getKey());
        config.set("size", gc.getSize());
        config.set("owner", gc.getOwner().getUniqueId());
        config.set("members", gc.getMembersUUIDs());
        config.set("moderators", gc.getModeratorsUUIDs());
        config.set("invited-players", gc.getPendingInviteesUUIDs());
        config.save(data_file);
    }

    public static void loadGroupChatFile(String key) {
        if (!LOADEDGROUPCHATS.contains(key)) {
            File data_file = new File(Attex.GROUPCHATSFOLDER, key + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);
            GroupChat.loadGroupChat(config);
            LOADEDGROUPCHATS.add(key);
        }
    }

    public static void saveGroupChatFile(GroupChat gc) throws IOException {
        File data_file = new File(Attex.PLAYERSETTINGSFOLDER, gc.getKey() + ".yml");
        if (!data_file.exists()) {createGroupChatFile(gc); return;}
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);

        for (int i=0; i < gc.getChanges().length; i++) {
            if (gc.getChanges()[i]) {
                switch (i) {
                    case 0 -> config.set("name", gc.getName());
                    case 1 -> config.set("size", gc.getSize());
                    case 2 -> config.set("owner", gc.getOwner().getUniqueId());
                    case 3 -> config.set("members", gc.getMembersUUIDs());
                    case 4 -> config.set("moderators", gc.getModeratorsUUIDs());
                    case 5 -> config.set("invited-players", gc.getPendingInviteesUUIDs());
                }
            }
        } config.save(data_file);
    }
}