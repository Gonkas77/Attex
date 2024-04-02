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

    private static void warnTemplate(Player p, String s) {
        p.sendMessage(PREFIX + "§c" + s);
        p.playSound(p, Sound.ENTITY_WITHER_SKELETON_HURT, SoundCategory.MASTER, 0.5f, 0.7f);
    }
    public static void customWarn(Player p, String s) {warnTemplate(p, s);}

    public static void invalidSubCommand(Player p) {warnTemplate(p, "Invalid sub-command inputted!");}
    public static void permissionWarn(Player p) {warnTemplate(p, "You don't have permission to do that!");}
    public static void offlinePlayerWarn(Player p) {warnTemplate(p, "This player is offline!");}

    public static void GCPromoteSelfWarn(Player p) {warnTemplate(p, "You can't promote yourself!");}
    public static void GCDeleteWarn(Player p) {warnTemplate(p, "You must be the Owner of the group chat to delete it!");}
    public static void GCInvalidInvite(Player p) {warnTemplate(p, "This invite code has either expired or is invalid!");}
}