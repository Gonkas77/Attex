package me.gonkas.attex.commands;

import me.gonkas.attex.Attex;
import me.gonkas.attex.chats.GroupChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChatSelector implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0 || args.length > 2) {
            commandSender.sendMessage("§9[Attex]§c This command requires one argument!");
            return true;
        }

        if (!(args[0].equals("local") || args[0].equals("party") || args[0].equals("team") || args[0].equals("private") || args[0].equals("group"))) {
            commandSender.sendMessage("§9[Attex]§c Invalid chat given! Use <local/team/party/private/group>.");
            return true;
        }

        if (args[0].equals("private")) {
            if (Bukkit.getPlayerExact(args[1]) == null) {
                commandSender.sendMessage("§9[Attex]§c Player is not online.");
                return true;
            } else {Attex.PLAYERSETTINGS.get((Player) commandSender).chat_target = args[1];}
        }

        if (args[0].equals("group")) {
            if (!GroupChat.getGroupChat((Player) commandSender, args[1]).getPlayers().contains((Player) commandSender)) {
                commandSender.sendMessage("§9[Attex]§c You are not in a group chat with that name.");
                return true;
            } else {Attex.PLAYERSETTINGS.get((Player) commandSender).chat_target = args[1];}
        }

        Attex.PLAYERSETTINGS.get((Player) commandSender).chat = args[0];
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }
}
