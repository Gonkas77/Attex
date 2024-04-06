package me.gonkas.attexdev.util;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class PlayerSendChat {

    public static String PREFIX = "§8[§bAttex§8] ";

    private static void infoTemplate(Player p, String s) {
        p.sendMessage(PREFIX + "§f" + s);
        p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1f, 1f);
    }
    public static void customInfo(Player p, String s) {infoTemplate(p, s);}

    public static void GCChat(Player p, String s) {infoTemplate(p, "You've set your default chat to Group Chat §b" + s + "§f!");}
    public static void GCCreate(Player p, String s) {infoTemplate(p, "You've created a new group chat named §b" + s + "§f! Use the \"§9/gc§f\" command to interact with it!");}
    public static void GCDelete(Player p, String s) {infoTemplate(p, "Group Chat §b" + s + "§f has been deleted.");}
    public static void GCDemoted(Player p, String s) {infoTemplate(p, "You have been demoted from moderator in the Group Chat §b" + s + "§f.");}
    public static void GCInvite(Player p, String s) {infoTemplate(p, "You have been invited to Group Chat §b" + s + "§f! Use \"§9/groupchat accept + " + s + "§f\" to accept it!");}
    public static void GCLeave(Player p, String s) {infoTemplate(p, "You have left the Group Chat §b" + s + "§f.");}
    public static void GCSelect(Player p, String s) {infoTemplate(p, "Selected group chat §b" + s + "§f. Use the \"§9/gc§f\" command to interact with it!");}
    public static void GCDeclineInvite(Player p, String s) {infoTemplate(p, "You declined the invite to the group chat §b" + s + "§f.");}


    private static void warnTemplate(Player p, String s) {
        p.sendMessage(PREFIX + "§c" + s);
        p.playSound(p, Sound.ENTITY_WITHER_SKELETON_HURT, SoundCategory.MASTER, 0.5f, 0.7f);
    }
    public static void customWarn(Player p, String s) {warnTemplate(p, s);}

    public static void invalidSubCommand(Player p) {warnTemplate(p, "Invalid sub-command inputted!");}
    public static void invalidPlayerName(Player p) {warnTemplate(p, "Invalid player inputted!");}
    public static void tooManyArgs(Player p, String s, int i) {warnTemplate(p, "This command only requires " + i + " arguments! Use /" + s + "!");}
    public static void tooFewArgs(Player p, String s, int i) {warnTemplate(p, "This command requires " + i + " arguments!  Use /" + s + "!");}
    public static void permissionWarn(Player p) {warnTemplate(p, "You don't have permission to do that!");}
    public static void offlinePlayerWarn(Player p) {warnTemplate(p, "This player is offline!");}
    public static void unknownError(Player p) {warnTemplate(p, "There was a problem performing this action. Try again later.");}
    public static void requiresConfirmation(Player p, String s) {warnTemplate(p, "Use §4\"/" + s + " confirm\"§c to confirm!");}

    public static void GCNotFound(Player p) {warnTemplate(p, "Unable to find inputted group chat.");}
    public static void GCPlayerNotFound(Player p) {warnTemplate(p, "Player is not in this group chat!");}
    public static void GCPromoteSelfWarn(Player p) {warnTemplate(p, "You can't promote yourself!");}
    public static void GCDemoteSelfWarn(Player p) {warnTemplate(p, "You can't demote yourself!");}
    public static void GCPermissionWarn(Player p) {warnTemplate(p, "You must be the Owner of the group chat to do this!");}
    public static void GCInvalidInvite(Player p) {warnTemplate(p, "This invite code has either expired or is invalid!");}
    public static void GCLackingInvite(Player p) {warnTemplate(p, "You do not have an invite to this group chat!");}
    public static void GCSizeLimitExceeded(Player p) {warnTemplate(p, "This group chat has reached its player maximum!");}
    public static void GCSameName(Player p) {warnTemplate(p, "You can't be in two group chats with the same name!");}
    public static void GCAlreadyModerator(Player p) {warnTemplate(p, "The player you're trying to promote is already a moderator!");}
    public static void GCNotModerator(Player p) {warnTemplate(p, "The player you're trying to demote is not a moderator!");}
    public static void GCInvalidRole(Player p) {warnTemplate(p, "Invalid role! Use §b\"/gc promote <player> <owner/moderator>\"§c!");}
}