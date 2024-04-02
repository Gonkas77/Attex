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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.gonkas.attex.util.Strings.compareStrings;

public class ChatSelector implements CommandExecutor, TabCompleter {

    private final List<String> chats = Arrays.stream(new String[]{"local", "party", "team", "private", "group"}).toList();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        if (args.length == 0 || args.length > 2) {
            Attex.playerSendWarn(player, "This command requires one argument!");
            return true;
        }

        if (!chats.contains(args[0])) {
            Attex.playerSendWarn(player, "Invalid chat given! Use <local/team/party/private/group>.");
            return true;
        }

        if (args[0].equals("private")) {
            if (Bukkit.getPlayerExact(args[1]) == null) {
                Attex.playerSendWarn(player, "Player is not online.");
                return true;
            } else {Attex.PLAYERSETTINGS.get(player).setChatTarget(args[1]);}
        }

        if (args[0].equals("group")) {
            if (!GroupChat.getGroupChat(player, args[1]).getPlayers().contains(player)) {
                Attex.playerSendWarn(player, "You are not in a group chat with that name.");
                return true;
            } else {Attex.PLAYERSETTINGS.get(player).setChatTarget(args[1]);}
        }

        Attex.PLAYERSETTINGS.get(player).setChatType(args[0]);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return compareStrings(args[0], chats);
        } else {return new ArrayList<>(0);}
    }
}
