package me.gonkas.attexdev;

import me.gonkas.attexdev.chats.GroupChat;
import me.gonkas.attexdev.data.DataManagement;
import me.gonkas.attexdev.data.PlayerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class Listeners implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) throws IOException {
        DataManagement.createPlayerSettingsFile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DataManagement.loadPlayerSettingsFile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws IOException {
        DataManagement.savePlayerSettingsFile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerSettings settings = Attex.PLAYERSETTINGS.get(player);
        if (settings.getChatType() == 'g') {
            event.setCancelled(true);
            GroupChat.getGroupChatWithKey(settings.getLastGCTargetKey()).message(player.getName(), event.getMessage());
        }
    }
}
