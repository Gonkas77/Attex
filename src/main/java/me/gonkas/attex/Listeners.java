package me.gonkas.attex;

import me.gonkas.attex.player.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

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
}
