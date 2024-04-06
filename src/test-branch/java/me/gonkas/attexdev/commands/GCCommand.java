package me.gonkas.attexdev.commands;

import me.gonkas.attexdev.Attex;
import me.gonkas.attexdev.chats.GroupChat;
import me.gonkas.attexdev.util.PlayerSendChat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.gonkas.attexdev.util.Strings.compareStrings;

public class GCCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        GroupChat gc = GroupChat.getGroupChatWithKey(Attex.PLAYERSETTINGS.get(player).getLastGCTargetKey());

        if (args.length == 0) {
            Attex.PLAYERSETTINGS.get(player).setChatType('g');
            PlayerSendChat.GCChat(player, gc.getName());
            return true;
        }

        switch (args[0]) {

            default: PlayerSendChat.invalidSubCommand(player); return true;

            case "chat":

                if (args.length >= 2) {PlayerSendChat.tooManyArgs(player, "gc chat", 1); return true;}
                Attex.PLAYERSETTINGS.get(player).setChatType('g');
                PlayerSendChat.GCChat(player, gc.getName());
                break;

            case "code":

                if (args.length >= 2) {PlayerSendChat.tooManyArgs(player, "gc code", 1); return true;}
                gc.generateInviteCode();
                gc.inviteCodeAnnouncement(player);
                break;

            case "delete":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc delete <confirm>",2); return true;}
                if (player == gc.getOwner()) {
                    if (args[1].equals("confirm")) {
                        PlayerSendChat.GCDelete(player, gc.getName());
                        gc.delete();
                    } else {PlayerSendChat.requiresConfirmation(player, "gc delete"); return true;}
                } else {PlayerSendChat.GCPermissionWarn(player); return true;}
                break;

            case "demote":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc demote <player>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc demote <player>", 2); return true;}
                if (args[1].length() > 16 || args[1].length() < 3) {PlayerSendChat.invalidPlayerName(player); return true;}

                Player demoted = Bukkit.getPlayerExact(args[1]);
                if (player != demoted) {
                    if (gc.getPlayers().contains(demoted)) {
                        if (player == gc.getOwner()) {
                            if (gc.getModerators().contains(demoted)) {
                                gc.removeModerator(demoted);
                                PlayerSendChat.GCDemoted(player, gc.getName());
                            } else {PlayerSendChat.GCNotModerator(player); return true;}
                        } else {PlayerSendChat.GCPermissionWarn(player); return true;}
                    } else {PlayerSendChat.GCPlayerNotFound(player); return true;}
                } else {PlayerSendChat.GCDemoteSelfWarn(player); return true;}
                break;

            case "invite":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc invite <player>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc invite <player>", 2); return true;}
                if (args[1].length() > 16 || args[1].length() < 3) {PlayerSendChat.invalidPlayerName(player); return true;}

                Player invited = Bukkit.getPlayerExact(args[1]);
                if (invited != null) {
                    gc.invitePlayer(player, invited);
                    gc.inviteAnnouncement(player, invited);
                } else {PlayerSendChat.offlinePlayerWarn(player); return true;}
                break;

            case "kick":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc kick <player>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc kick <player>", 2); return true;}
                if (args[1].length() > 16 || args[1].length() < 3) {PlayerSendChat.invalidPlayerName(player); return true;}

                Player kicked = Bukkit.getPlayerExact(args[1]);
                if (kicked != null) {
                    if (gc.getPlayers().contains(kicked)) {
                        gc.removePlayer(kicked);
                        gc.kickAnnouncement(kicked);
                    } else {PlayerSendChat.GCPlayerNotFound(player); return true;}
                } else {PlayerSendChat.offlinePlayerWarn(player); return true;}
                break;

            case "leave":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc leave <confirm>", 2); return true;}
                if (args[1].equals("confirm")) {
                    gc.removePlayer(player);
                    gc.leaveAnnouncement(player);
                    PlayerSendChat.GCLeave(player, gc.getName());
                } else {PlayerSendChat.requiresConfirmation(player, "gc leave"); return true;}
                break;

            case "promote":

                if (args.length >= 5 && args[2].equals("owner")) {PlayerSendChat.tooManyArgs(player, "gc promote <player> owner <confirm>", 4); return true;}
                if (args.length >= 4 && args[2].equals("moderator")) {PlayerSendChat.tooManyArgs(player, "gc promote <player> moderator", 3); return true;}
                if (args.length >= 5) {PlayerSendChat.tooManyArgs(player, "gc promote <player> <role>", 3); return true;}
                if (args.length <= 2) {PlayerSendChat.tooFewArgs(player, "gc promote <player> <role>", 3); return true;}
                if (args[1].length() > 16 || args[1].length() < 3) {PlayerSendChat.invalidPlayerName(player); return true;}

                Player promoted = Bukkit.getPlayerExact(args[1]);
                if (player != promoted) {
                    if (gc.getPlayers().contains(promoted)) {
                        if (player == gc.getOwner()) {
                            if (args[2].equals("owner")) {
                                if (args[3].equals("confirm")) {
                                    gc.setOwner(promoted);
                                    gc.promotionAnnouncement(player, true);
                                } else {PlayerSendChat.requiresConfirmation(player, "gc promote <player> owner confirm");}
                            } else if (args[2].equals("moderator")) {
                                if (!gc.getModerators().contains(promoted)) {
                                    gc.addModerator(promoted);
                                    gc.promotionAnnouncement(promoted, false);
                                } else {PlayerSendChat.GCAlreadyModerator(player); return true;}
                            } else {PlayerSendChat.GCInvalidRole(player); return true;}
                        } else {PlayerSendChat.GCPermissionWarn(player); return true;}
                    } else {PlayerSendChat.GCPlayerNotFound(player); return true;}
                } else {PlayerSendChat.GCPromoteSelfWarn(player); return true;}
                break;

            case "rename":

                if (args.length >= 3) {PlayerSendChat.tooManyArgs(player, "gc rename <name>", 2); return true;}
                if (args.length == 1) {PlayerSendChat.tooFewArgs(player, "gc rename <name>", 2); return true;}

                if (player == gc.getOwner()) {
                    gc.rename(args[1]);
                    gc.renameAnnouncement();
                } else {PlayerSendChat.GCPermissionWarn(player); return true;}
                break;

        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        GroupChat gc = GroupChat.getGroupChatWithKey(Attex.PLAYERSETTINGS.get((Player) commandSender).getLastGCTargetKey());

        return switch (args.length) {
            default -> new ArrayList<>(0);
            case 1 -> compareStrings(args[0], new String[]{"chat", "code", "delete", "demote", "invite", "kick", "leave", "promote", "rename"});
            case 2 -> {
                switch (args[0]) {
                    default: yield new ArrayList<>(0);
                    case "delete", "leave": yield compareStrings(args[1], new ArrayList<>(List.of("confirm")));
                    case "demote": yield compareStrings(args[1], gc.getModeratorNames());
                    case "invite": yield new ArrayList<>(List.of("<player>"));
                    case "kick", "promote": yield compareStrings(args[1], gc.getPromotableNames());
                    case "rename": yield new ArrayList<>(List.of("<name>"));
                }
            }
            case 3 -> {

                if (args[0].equals("promote") && gc.getPromotableNames().contains(args[1])) {
                    HashMap<String, Boolean> map = gc.getPromotable();
                    if (map.containsKey(args[1])) {
                        if (map.get(args[1])) {yield compareStrings(args[2], new ArrayList<>(List.of("moderator", "owner")));}
                        else {yield compareStrings(args[2], new ArrayList<>(List.of("owner")));}
                    }
                } yield new ArrayList<>(0);
            }
            case 4 -> {
                if (args[0].equals("promote") && gc.getPromotableNames().contains(args[1]) && !gc.getPromotable().get(args[1])) {
                    yield compareStrings(args[3], new ArrayList<>(List.of("confirm")));
                } yield new ArrayList<>(0);
            }
        };
    }
}