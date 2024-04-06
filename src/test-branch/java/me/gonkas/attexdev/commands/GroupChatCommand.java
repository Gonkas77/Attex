package me.gonkas.attexdev.commands;

import me.gonkas.attexdev.Attex;
import me.gonkas.attexdev.chats.GroupChat;
import me.gonkas.attexdev.util.PlayerSendChat;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.gonkas.attexdev.util.Strings.compareStrings;
import static me.gonkas.attexdev.util.Strings.convertKeyListToGCNameList;

public class GroupChatCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        if (args.length == 0) {PlayerSendChat.tooFewArgs(player, "gc <sub-command>", 1); return true;}

        switch (args[0]) {

            default: PlayerSendChat.invalidSubCommand(player); return true;

            case "accept":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc accept <group chat>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc accept <group chat>", 2); return true;}

                ArrayList<String> gcs = convertKeyListToGCNameList(Attex.PLAYERSETTINGS.get(player).getGroupChatKeyList());
                if (Attex.PLAYERSETTINGS.get(player).getGroupChatInvitesKeyList().isEmpty()) {PlayerSendChat.GCLackingInvite(player);}
                else {
                    for (String key : Attex.PLAYERSETTINGS.get(player).getGroupChatInvitesKeyList()) {
                        GroupChat gc = GroupChat.getGroupChatWithKey(key);
                        if (gcs.contains(args[1])) {PlayerSendChat.GCSameName(player); return true;}

                        if (args[1].equals(gc.getName())) {
                            try {
                                gc.addPlayer(player);
                                gc.joinAnnouncement(player);
                            } catch (SizeLimitExceededException ignored) {PlayerSendChat.GCSizeLimitExceeded(player);}
                        }
                    } PlayerSendChat.GCLackingInvite(player);
                } break;

            case "create":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc create <name>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc create <name>", 2); return true;}

                if (!convertKeyListToGCNameList(Attex.PLAYERSETTINGS.get(player).getGroupChatKeyList()).contains(args[1])) {
                    new GroupChat(player, args[1]);
                    PlayerSendChat.GCCreate(player, args[1]);
                } else {PlayerSendChat.GCSameName(player);}
                break;

            case "decline":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc decline <group chat>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc decline <group chat>", 2); return true;}

                for (String key : Attex.PLAYERSETTINGS.get(player).getGroupChatInvitesKeyList()) {
                    GroupChat gc = GroupChat.getGroupChatWithKey(key);
                    if (args[1].equals(gc.getName())) {
                        gc.removeInvite(player);
                        Attex.PLAYERSETTINGS.get(player).getGroupChatInvitesKeyList().remove(gc.getName());
                        PlayerSendChat.GCDeclineInvite(player, gc.getName());
                        break;
                    }
                } PlayerSendChat.GCLackingInvite(player);
                break;

            case "join":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc join <code>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc join <code>", 2); return true;}

                try {GroupChat.useCode(player, args[1]).joinAnnouncement(player);}
                catch (SizeLimitExceededException ignored) {PlayerSendChat.GCSizeLimitExceeded(player);}
                break;

            case "list":

                if (args.length >= 2) {PlayerSendChat.tooManyArgs(player, "gc list", 1); return true;}

                player.sendMessage("");
                player.sendMessage("§bList of Group Chats you are in:");
                player.sendMessage(Attex.PLAYERSETTINGS.get(player).getGroupChatNamesToString());
                player.sendMessage("");
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1f);
                break;

            case "select":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc select <group chat>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc select <group chat>", 2); return true;}

                GroupChat gc = GroupChat.getGroupChat(player, args[1]);
                if (gc != null) {
                    try {
                        Attex.PLAYERSETTINGS.get(player).setGCTarget(gc.getKey());
                        PlayerSendChat.GCSelect(player, gc.getName());
                    } catch (IOException ignored) {PlayerSendChat.unknownError(player);}
                } else {PlayerSendChat.GCNotFound(player);
                } break;

        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {return compareStrings(args[0], new String[]{"accept", "create", "decline", "join", "list", "select"});}
        if (args.length == 2) {
            return switch (args[0]) {
                default -> new ArrayList<>(0);
                case "accept", "decline" -> compareStrings(args[1], convertKeyListToGCNameList(Attex.PLAYERSETTINGS.get((Player) commandSender).getGroupChatInvitesKeyList()));
                case "create" -> new ArrayList<>(List.of(new String[]{"<name>"}));
                case "join" -> new ArrayList<>(List.of(new String[]{"<code>"}));
                case "select" -> compareStrings(args[1], convertKeyListToGCNameList(Attex.PLAYERSETTINGS.get((Player) commandSender).getGroupChatKeyList()));
            };
        } return new ArrayList<>(0);
    }
}