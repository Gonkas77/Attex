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

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupChatCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        if (args.length <= 2) {
            Attex.playerSendWarn(player, "This command requires at least 2 arguments!");
            return true;
        }

        switch (args[0]) {

            case "accept":
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
                } break;

            case "create":
                new GroupChat(args[1], player);
                break;

            case "join":
                try {
                    GroupChat gc = GroupChat.useCode(player, args[1]);
                    gc.joinAnnouncement(player);
                } catch (SizeLimitExceededException ignored) {
                    Attex.playerSendWarn(player, "This group has reached the player limit!");
                    return true;
                } break;

            default:
                GroupChat group = GroupChat.getGroupChat(player, args[0]);
                if (!group.getPlayers().contains(player)) {
                    Attex.playerSendWarn(player, "You are not in a group chat with that name!");
                    return true;
                } else {

                    switch (args[1]) {

                        default:
                            Attex.playerSendWarn(player, "Invalid sub-command inputted!");
                            break;

                        case "chat":
                            Attex.PLAYERSETTINGS.get(player).chat = "group";
                            Attex.PLAYERSETTINGS.get(player).chat_target = args[0];
                            break;

                        case "code":
                            group.inviteCodeAnnouncement(player);
                            break;

                        case "delete":

                            if (group.getOwner() == player) {
                                if (args[2].equals("confirm")) {
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

                            Player addition = Bukkit.getPlayerExact(args[2]);
                            if (addition != null) {
                                group.invitePlayer(player, addition);
                                group.inviteAnnouncement(player, addition);
                            } else {
                                Attex.playerSendWarn(player, "This player is offline!");
                                return true;
                            } break;

                        case "leave":

                            if (args[2].equals("confirm")) {
                                group.removePlayer(player);
                                group.leaveAnnouncement(player);
                            } else {
                                Attex.playerSendWarn(player, "Use §4\"/groupchat <groupchat> leave confirm\"§c to confirm!");
                                return true;
                            } break;

                        case "promote":

                            Player promoted = Bukkit.getPlayerExact(args[2]);
                            if (group.getPlayers().contains(promoted)) {
                                if (args[3].equals("owner")) {
                                    if (group.getOwner() != promoted) {
                                        group.setOwner(promoted);
                                        group.promotionAnnouncement(promoted, true);
                                    } else {
                                        group.info(player, "§cYou are already the owner of this Group Chat!");
                                        return true;
                                    }
                                } else if (args[3].equals("moderator")) {
                                    if (!group.getModerators().contains(promoted)) {
                                        group.addModerator(promoted);
                                        group.promotionAnnouncement(promoted, false);
                                    } else {
                                        group.info(player, "§cThis player is already a moderator!");
                                        return true;
                                    }
                                } else {
                                    Attex.playerSendWarn(player, "Invalid role inputted!");
                                    return true;
                                }
                            } else {
                                Attex.playerSendWarn(player, "This player is not in this Group Chat!");
                                return true;
                            } break;

                        case "unpromote":

                            Player unpromoted = Bukkit.getPlayerExact(args[2]);
                            if (group.getPlayers().contains(unpromoted)) {
                                if (group.getModerators().contains(unpromoted)) {
                                    group.removeModerator(unpromoted);
                                    group.info(player, "§b" + unpromoted.getName() + "§f is no longer a moderator.");
                                } else {
                                    Attex.playerSendWarn(player, "This player is not a moderator of this Group Chat!");
                                    return true;
                                }
                            } else {
                                Attex.playerSendWarn(player, "This player is not in this Group Chat!");
                                return true;
                            } break;
                    }
                }
        } return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player player = (Player) commandSender;
        ArrayList<String> options = new ArrayList<>(0);
        for (GroupChat gc : Attex.PLAYERGC.get(player)) {options.add(gc.getName());}

        if (args.length == 1) {
            options.addAll(Arrays.stream(new String[]{"accept", "create", "join"}).toList());
            if (args[0].isEmpty()) {return options;}
            else {return compareStrings(args[0], options);}

        } else if (args.length == 2) {
            if (options.contains(args[0])) {
                return compareStrings(args[1], Arrays.stream(new String[]{"chat", "code", "delete", "invite", "leave", "promote"}).toList());
            } else {
                return switch (args[0]) {
                    case "accept" -> {
                        ArrayList<String> invites = new ArrayList<>();
                        for (GroupChat gc : Attex.PLAYERGCINVITES.get(player)) {invites.add(gc.getName());}
                        yield compareStrings(args[1], invites);
                    }
                    case "create" -> Arrays.stream(new String[]{"<name>"}).toList();
                    case "join" -> Arrays.stream(new String[]{"<code>"}).toList();
                    default -> new ArrayList<>(0);
                };
            }

        } else if (args.length == 3) {
            if (options.contains(args[0])) {
                return switch (args[1]) {
                    case "delete", "leave" -> compareStrings(args[2], Arrays.stream(new String[]{"confirm"}).toList());
                    case "invite" -> Arrays.stream(new String[]{"<player>"}).toList();
                    case "promote" -> compareStrings(args[2], GroupChat.getGroupChat(player, args[0]).getPromotable().keySet().stream().toList());
                    default -> new ArrayList<>(0);
                };
            }
        } else if (args.length == 4) {
            if (options.contains(args[0])) {
                if (args[1].equals("promote")) {
                    if (GroupChat.getGroupChat(player, args[0]).getPromotable().get(args[2])) {return Arrays.stream(new String[]{"moderator", "owner"}).toList();}
                    else {return Arrays.stream(new String[]{"owner"}).toList();}
                }
            }
        } return new ArrayList<>(0);
    }

    public static List<String> compareStrings(String input, List<String> strings) {
        ArrayList<String> candidates = new ArrayList<>(0);
        ArrayList<String> matches = new ArrayList<>(0);

        for (String s : strings) {
            try {candidates.add(s.substring(0, input.length()-1));}
            catch (IndexOutOfBoundsException ignored) {candidates.add(null);}
        }
        for (int i=0; i < candidates.size(); i++) {
            if (candidates.get(i) != null) {
                if (candidates.get(i).equals(input)) {matches.add(strings.get(i));}
            }
        } return matches;
    }
}