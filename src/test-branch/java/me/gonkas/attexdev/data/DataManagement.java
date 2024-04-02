package me.gonkas.attexdev.data;

import me.gonkas.attexdev.Attex;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManagement {

    public static void createPlayerSettingsFile(Player player) throws IOException, InaccessibleObjectException {
        String uuid = player.getUniqueId().toString();
        File data_file = new File(Attex.PLAYERSETTINGSFOLDER, uuid + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);

        config.set("last-username", player.getName());
        config.set("info.chats.type", "l"); // 'l' == local; 't' == team; 'p' == party
        config.set("info.chats.last-pm", uuid);
        config.set("info.groupchats.last-gc", "");
        config.set("info.groupchats.list", new ArrayList<String>(0));
        config.set("info.groupchats.invites", new ArrayList<String>(0));
        config.set("info.friends.list", Arrays.stream(new String[]{uuid.toString()}).toList()); // delete this later on !!!!!!!!!!!!!!!!!!
        config.save(data_file);
    }

    public static void loadPlayerSettingsFile(Player player) {
        File data_file = new File(Attex.PLAYERSETTINGSFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);
        Attex.PLAYERSETTINGS.put(player, new PlayerSettings(player, config));
    }

    public static void savePlayerSettingsFile(Player player) throws IOException {
        File data_file = new File(Attex.PLAYERSETTINGSFOLDER, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data_file);

        if (!player.getName().equals(config.getString("last-username"))) {config.set("last-username", player.getName());}

        PlayerSettings settings = Attex.PLAYERSETTINGS.get(player);
        for (int i=0; i < settings.getChanges().length; i++) {
            if (settings.getChanges()[i]) {
                switch (i) {
                    case 0 -> config.set("info.chats.type", settings.getChatType());
                    case 1 -> config.set("info.chats.last-pm", settings.getLastPMTarget());
                    case 2 -> config.set("info.groupchats.last-gc", settings.getLastGCTargetKey());
                    case 3 -> config.set("info.groupchats.list", settings.getGroupChatList());
                    case 4 -> config.set("info.groupchats.invites", settings.getGroupChatInviteList());
                    case 5 -> config.set("info.friends.list", settings.getFriendList());
                }
            }
        } config.save(data_file);
    }
}
