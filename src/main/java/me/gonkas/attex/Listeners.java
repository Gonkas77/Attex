package me.gonkas.attex;

import me.gonkas.attex.chats.GroupChat;
import me.gonkas.attex.player.PlayerData;
import me.gonkas.attex.player.PlayerSettings;
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
        PlayerData.createFile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData.loadFile(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws IOException {
        PlayerData.saveData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerSettings settings = Attex.PLAYERSETTINGS.get(player);
        if (settings.getSelectedChat().equals("group")) {
            event.setCancelled(true);
            GroupChat.getGroupChatWithKey(settings.getChatTarget()).message(event.getMessage());
        }
    }
}