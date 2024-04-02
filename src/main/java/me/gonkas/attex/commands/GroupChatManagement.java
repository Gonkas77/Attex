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

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.gonkas.attex.util.Strings.compareStrings;

public class GroupChatManagement implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        if (args.length == 0) {
            Attex.playerSendWarn(player, "This command requires 1 argument!");
            return true;
        } else if (args.length > 2) {
            Attex.playerSendWarn(player, "This command requires only 2 arguments!");
            return true;
        }

        switch (args[0]) {

            default:
                Attex.playerSendWarn(player, "Invalid sub-command! Use <accept/create/join/select>.");
                return true;

            case "accept":
                if (Attex.PLAYERGCINVITES.get(player).isEmpty()) {Attex.playerSendWarn(player, "You do not have an invite for that group!"); return true;}
                else {
                    for (GroupChat gc : Attex.PLAYERGCINVITES.get(player)) {
                        if (args[1].equals(gc.getName())) {
                            try {
                                gc.addPlayer(player);
                                gc.joinAnnouncement(player);
                            }
                            catch (SizeLimitExceededException ignored) {
                                Attex.playerSendWarn(player, "This group has reached the player limit!");
                                return true;
                            }
                        } else {Attex.playerSendWarn(player, "You do not have an invite for that group!"); return true;}
                    }
                } break;

            case "create":
                Attex.playerSendInfo(player, "You have created a new group chat named §9" + args[1] + "§f!");
                new GroupChat(args[1], player);
                break;

            case "join":
                try {GroupChat.useCode(player, args[1]).joinAnnouncement(player);}
                catch (SizeLimitExceededException ignored) {
                    Attex.playerSendWarn(player, "This group has reached the player limit!");
                    return true;
                } break;

            case "select":

                GroupChat group = GroupChat.getGroupChat(player, args[1]);
                if (group != null) {Attex.PLAYERSETTINGS.get(player).setGroupChatTarget(group);}
                else {
                    Attex.playerSendWarn(player, "Invalid group chat!");
                    return true;
                } break;

        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;

        if (args.length == 1) {
            return compareStrings(args[0], new String[]{"accept", "create", "select", "join"});

        } else if (args.length == 2) {

            switch (args[0]) {
                default: return new ArrayList<>();
                case "accept":
                    ArrayList<String> invites = new ArrayList<>();
                    for (GroupChat gc : Attex.PLAYERGCINVITES.get(player)) {
                        invites.add(gc.getName());
                    } return compareStrings(args[1], invites);

                case "create": return Arrays.stream(new String[]{"<name>"}).toList();
                case "join": return Arrays.stream(new String[]{"<code>"}).toList();

                case "select":
                    ArrayList<String> gcs = new ArrayList<>();
                    for (GroupChat gc : Attex.PLAYERGC.get(player)) {
                        gcs.add(gc.getName());
                    } return compareStrings(args[1], gcs);
            }

        } return new ArrayList<>();
    }
}