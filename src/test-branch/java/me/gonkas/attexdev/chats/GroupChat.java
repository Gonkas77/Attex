package me.gonkas.attexdev.chats;

import me.gonkas.attexdev.Attex;
import me.gonkas.attexdev.util.PlayerSendChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.naming.SizeLimitExceededException;
import java.io.File;
import java.util.*;

import static me.gonkas.attexdev.util.Strings.convertStringListToPlayerArrayList;

public class GroupChat {

    private static HashMap<String, GroupChat> GROUPCHATS = new HashMap<>();
    private static HashMap<String, GroupChat> GROUPCHATCODES = new HashMap<>();
    private static HashMap<String, ArrayList<String>> GROUPCHATKEYS = new HashMap<>();

    private String name;
    private String key;
    private Player owner;

    private int size = 1;

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> moderators = new ArrayList<>();
    private ArrayList<Player> pending_invites = new ArrayList<>();
    private String active_code;

    public GroupChat(String name, Player owner) {
        this.name = name;
        this.owner = owner;
        this.players.add(owner);

        String key;
        if (GROUPCHATKEYS.containsKey(name)) {
            key = name.toUpperCase() + "_" + (GROUPCHATKEYS.get(name).size());
            GROUPCHATKEYS.get(name).add(key);
        } else {
            key = name.toUpperCase() + "_0";
            GROUPCHATKEYS.put(name, new ArrayList<>(List.of(new String[]{key})));
        }
        this.key = key;
        GROUPCHATS.put(key, this);
        Attex.PLAYERGC.get(owner).add(this.getKey());
    }

    private GroupChat() {
        this.players = new ArrayList<Player>(0);
    }

    private GroupChat(YamlConfiguration config) {
        this.name = config.getString("name");
        this.key = config.getString("key");
        this.owner = Bukkit.getPlayer(config.getObject("owner", UUID.class));
        this.size = config.getInt("size");
        this.players.addAll(convertStringListToPlayerArrayList(config.getStringList("players")));
        this.moderators.addAll(convertStringListToPlayerArrayList(config.getStringList("moderators")));
        this.pending_invites.addAll(convertStringListToPlayerArrayList(config.getStringList("invited-players")));
    }

    public static void loadGroupChat(String key) {
        File gc_file = new File(Attex.GROUPCHATSFOLDER, key);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(gc_file);
        GROUPCHATS.put(key, new GroupChat(config));

        String name = config.getString("name");
        if (GROUPCHATKEYS.containsKey(name)) {GROUPCHATKEYS.get(name).add(key);}
        else {GROUPCHATKEYS.put(name, new ArrayList<>(List.of(new String[]{key})));}
    }

    public static GroupChat getGroupChat(Player player, String s) {
        for (String key : GROUPCHATKEYS.get(s)) {
            if (GROUPCHATS.get(key).getPlayers().contains(player)) {return GROUPCHATS.get(key);}
        } return new GroupChat();
    }
    public static GroupChat getGroupChatWithKey(String key) {
        if (GROUPCHATS.containsKey(key)) {return GROUPCHATS.get(key);}
        else {return new GroupChat();}
    }
    public boolean isGhost() {return (this.key == null);}

    public String getName() {return this.name;}
    public String getKey() {return this.key;}
    public Player getOwner() {return this.owner;}
    public ArrayList<Player> getPlayers() {return this.players;}
    public ArrayList<String> getMemberNames() {
        ArrayList<String> names = new ArrayList<>(0);
        for (Player player : this.players) {names.add(player.getName());}
        return names;
    }
    public ArrayList<Player> getModerators() {return this.moderators;}
    public ArrayList<String> getModeratorNames() {
        ArrayList<String> names = new ArrayList<>(0);
        for (Player player : this.moderators) {names.add(player.getName());}
        return names;
    }
    public HashMap<String, Boolean> getPromotable() {
        HashMap<String, Boolean> map = new HashMap<>();
        for (String mod : getModeratorNames()) {map.put(mod, false);}
        for (String mem : getMemberNames()) {map.put(mem, true);}
        return map;
    }

    public void addPlayer(Player player) throws SizeLimitExceededException {
        if (this.size < 50) {
            this.players.add(player);
            this.size++;
        } else {throw new SizeLimitExceededException();}
    }
    public void removePlayer(Player player) {
        Attex.PLAYERGC.get(player).remove(this);
        this.players.remove(player);
        this.size--;
    }
    public void invitePlayer(Player player, Player invited) {
        if (this.pending_invites.size() < 10) {this.pending_invites.add(invited);}
        else {this.info(player, "You have reached the pending invites limit!");}
        Attex.PLAYERGCINVITES.get(invited).add(this.getKey());
    }
    public void removeInvite(Player player) {
        this.pending_invites.remove(player);
    }
    public void setOwner(Player player) {
        this.owner = player;
    }
    public void addModerator(Player player) {this.moderators.add(player);}
    public void removeModerator(Player player) {this.moderators.remove(player);}

    public void delete() {
        for (Player player : this.players) {Attex.PLAYERGC.get(player).remove(this);}
        for (Player player : this.pending_invites) {Attex.PLAYERGCINVITES.get(player).remove(this);}
        GROUPCHATCODES.remove(this.active_code, this);
        GROUPCHATKEYS.remove(this.name);
        GROUPCHATS.remove(this.key);
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
    public static GroupChat useCode(Player player, String code) throws SizeLimitExceededException {
        if (GROUPCHATCODES.containsKey(code)) {
            GROUPCHATCODES.get(code).addPlayer(player);
            return GROUPCHATCODES.get(code);
        } else {
            PlayerSendChat.GCInvalidInvite(player);
            return new GroupChat();
        }
    }

    public void info(String message) {
        for (Player player : this.players) {
            player.sendMessage("§7[§bGC§7\\§9" + this.getName() + "§7]§f " + message);
        }
    }
    public void info(Player player, String message) {
        player.sendMessage("§7[§bGC§7\\§9" + this.getName() + "§7]§f " + message);
    }
    public void message(String sender, String message) {
        for (Player player : getPlayers()) {
            player.sendMessage("§7[§bGC§7\\§9" + getName() + "§7] <§8" + sender + "§7> §8" + message);
        }
    }
    public void joinAnnouncement(Player player) {
        this.info("Player §b" + player.getName() + "§f has joined this group chat!");
    }
    public void leaveAnnouncement(Player player) {
        this.info("Player §b" + player.getName() + "§f has left this group chat!");
    }
    public void promotionAnnouncement(Player player, boolean bool) {
        if (bool) {this.info("Player §b" + player.getName() + "§f has become the owner of this group chat!");}
        else {this.info("Player §b" + player.getName() + "§f has become a moderator of this group chat!");}
    }
    public void inviteCodeAnnouncement(Player player) {
        this.info("A new invite code §b" + generateInviteCode() + "§f has been generated by §b" + player.getName() + "§f.\n It will be active for 5 minutes.");
    }
    public void inviteAnnouncement(Player player, Player invited) {
        this.info("§b" + invited.getName() + "§f has been invited to this Group Chat by §b" + player.getName() + "§f.");
    }
}