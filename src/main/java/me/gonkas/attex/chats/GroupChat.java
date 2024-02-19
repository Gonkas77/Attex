package me.gonkas.attex.chats;

import me.gonkas.attex.Attex;
import org.bukkit.entity.Player;

import javax.naming.SizeLimitExceededException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GroupChat {

    private static HashMap<String, GroupChat> GROUPCHATS = new HashMap<>();
    private static HashMap<String, GroupChat> GROUPCHATCODES = new HashMap<>();
    private static HashMap<String, ArrayList<String>> GROUPCHATKEYS = new HashMap<>();

    private String name;
    private String key;
    private Player owner;

    private int size = 1;
    private boolean privacy = true;

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> pending_invites = new ArrayList<>();
    private String active_code;

    public GroupChat(String name, Player owner) {
        this.name = name;
        this.owner = owner;
        this.players.add(owner);

        String key;
        if (GROUPCHATKEYS.containsKey(name)) {
            key = name + "_" + (GROUPCHATKEYS.get(name).size() - 1);
            GROUPCHATKEYS.get(name).add(key);
        } else {
            key = name + "_0";
            GROUPCHATKEYS.put(name, new ArrayList<String>(List.of(new String[]{key})));
        }
        this.key = key;
        GROUPCHATS.put(key, this);
    }

    private GroupChat() {
        this.players = new ArrayList<Player>(0);
    }

    public static GroupChat getGroupChat(Player player, String s) {
        for (String key : GROUPCHATKEYS.get(s)) {
            if (GROUPCHATS.get(key).players.contains(player)) {return GROUPCHATS.get(key);}
        } return new GroupChat();
    }
    public String getName() {return this.name;}
    public Player getOwner() {return this.owner;}
    public ArrayList<Player> getPlayers() {return this.players;}

    public void addPlayer(Player player) throws SizeLimitExceededException {
        if (this.size < 50) {
            this.players.add(player);
            this.size++;
        } else {throw new SizeLimitExceededException();}
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
        this.size--;
    }
    public void invitePlayer(Player player) {
        if (this.pending_invites.size() < 10) {this.pending_invites.add(player);}
    }
    public void removeInvite(Player player) {
        this.pending_invites.remove(player);
    }
    public void setOwner(Player player) {
        this.owner = player;
    }

    public void delete() {
        for (Player player : this.players) {Attex.PLAYERGC.get(player).remove(this);}
        for (Player player : this.pending_invites) {Attex.PLAYERINVITES.get(player).remove(this);}
        GROUPCHATCODES.remove(this.active_code, this);
        GROUPCHATKEYS.remove(this.name);
        GROUPCHATS.remove(this.key);
    }
    public void togglePrivacy() {
        this.privacy = !this.privacy;
    }
    public String generateInviteCode() {
        char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuilder code = new StringBuilder();
        for (int num : new Random().ints(8, 0, 35).toArray()) {code.append(characters[num]);}

        String result = code.toString();
        GROUPCHATCODES.put(result, this);
        this.active_code = result;
        return result;
    }

    public static String groupChatMessage(GroupChat gc, Player player, String message) {
        return "§7[§bGC§7\\§9" + gc.getName() + "§7] <§8" + player.getName() + "§7> §8" + message;
    }
    public static String groupChatInfo(GroupChat gc, String message) {
        return "§7[§bGC§7\\§9" + gc.getName() + "§7]§f " + message;
    }

}