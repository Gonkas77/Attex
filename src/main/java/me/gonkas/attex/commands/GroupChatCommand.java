package me.gonkas.attex.commands;

import me.gonkas.attex.Attex;
import me.gonkas.attex.chats.GroupChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GroupChatCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        GroupChat group = null;
        if (!(args[0].equals("create") || args[0].equals("accept") || args[0].equals("join"))) {
            Player player = (Player) commandSender;
            for (GroupChat gc : Attex.PLAYERGC.get(player)) {
                if (args[0].equals(gc.getName())) {group = gc; break;}
            } if (group == null) {commandSender.sendMessage("§9[Attex]§c You are not in a group with that name!"); return true;}
        }

        switch (args[0]) {
            case "accept":
                for (GroupChat gc : Attex.PLAYERINVITES.get((Player) commandSender)) {
                    if (args[1].equals(gc.getName())) {
                        TO BE COMPLETED
                    }
                }
        }

        switch (args[1]) {
            default:
                commandSender.sendMessage("§9[Attex]§c You are not in a group with that name!");
                break;
            case "chat":
                Attex.PLAYERSETTINGS.get((Player) commandSender).chat = "group";
                Attex.PLAYERSETTINGS.get((Player) commandSender).chat_target = args[0];
                break;
            case "code":
                commandSender.sendMessage(GroupChat.groupChatInfo(group, "You can use the invite code §b" + group.generateInviteCode() + "§f to join this group for the next 5 minutes."));
                break;
            case "delete":
                if (group.getOwner() == commandSender) {
                    if (args[2].equals("confirm")) {group.delete();}
                    else {commandSender.sendMessage(GroupChat.groupChatInfo(group, "§cUse §4\"/groupchat <groupchat> delete confirm\"§c to confirm!"));}
                } else {
                    commandSender.sendMessage(GroupChat.groupChatInfo(group, "§cYou must be the Owner to delete this Group Chat!"));
                } break;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return null;
    }
}