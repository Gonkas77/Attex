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

public class GroupChatCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        GroupChat group = GroupChat.getGroupChatWithKey(Attex.PLAYERSETTINGS.get(player).getSelectedGroup());

        switch (args[0]) {

            default:
                Attex.playerSendWarn(player, "Invalid sub-command inputted!");
                break;

            case "chat":
                Attex.PLAYERSETTINGS.get(player).setChatType("group");
                Attex.PLAYERSETTINGS.get(player).setChatTarget(args[0]);
                break;

            case "code":
                group.inviteCodeAnnouncement(player);
                break;

            case "delete":

                if (group.getOwner() == player) {
                    if (args[1].equals("confirm")) {
                        Attex.playerSendInfo(player, "Group Chat §b" + group.getName() + "§f has been deleted.");
                        group.delete();
                    } else {
                        Attex.playerSendWarn(player, "Use §4\"/groupchat <groupchat> delete confirm\"§c to confirm!");
                        return true;
                    }
                } else {
                    Attex.playerSendWarn(player, "You must be the Group Chat's owner to delete it!");
                    return true;
                } break;

            case "invite":

                Player addition = Bukkit.getPlayerExact(args[1]);
                if (addition != null) {
                    group.invitePlayer(player, addition);
                    group.inviteAnnouncement(player, addition);
                } else {
                    Attex.playerSendWarn(player, "This player is offline!");
                    return true;
                } break;

            case "leave":

                if (args[1].equals("confirm")) {
                    group.removePlayer(player);
                    group.leaveAnnouncement(player);
                } else {
                    Attex.playerSendWarn(player, "Use §4\"/groupchat <groupchat> leave confirm\"§c to confirm!");
                    return true;
                } break;

            case "promote":

                Player promoted = Bukkit.getPlayerExact(args[1]);
                if (player != promoted) {
                    if (group.getOwner() == player) {
                        if (group.getPlayers().contains(promoted)) {
                            if (args[2].equals("owner")) {
                                group.setOwner(promoted);
                                group.promotionAnnouncement(promoted, true);
                            } else if (args[2].equals("moderator")) {
                                group.addModerator(promoted);
                                group.promotionAnnouncement(promoted, false);
                            }
                        } else {
                            Attex.playerSendWarn(player, "This player is not in this group chat!");
                            return true;
                        }
                    } else {
                        Attex.playerSendWarn(player, "You don't have permission to promote anyone in this group chat!");
                        return true;
                    }
                } else {
                    Attex.playerSendWarn(player, "You can't promote yourself!");
                    return true;
                } break;

            case "demoted":

                Player demoted = Bukkit.getPlayerExact(args[1]);
                if (player != demoted) {
                    if (group.getOwner() == player) {
                        if (group.getPlayers().contains(demoted)) {
                            group.removeModerator(demoted);
                        } else {
                            Attex.playerSendWarn(player, "This player is not in this group chat!");
                            return true;
                        }
                    } else {
                        Attex.playerSendWarn(player, "You don't have permission to promote anyone in this group chat!");
                        return true;
                    }
                } else {
                    Attex.playerSendWarn(player, "You can't demote yourself!");
                    return true;
                } break;

        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
