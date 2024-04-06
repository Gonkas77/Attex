package me.gonkas.attexdev.chats;

import me.gonkas.attexdev.Attex;
import me.gonkas.attexdev.data.DataManagement;
import me.gonkas.attexdev.util.PlayerSendChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.naming.SizeLimitExceededException;
import java.io.IOException;
import java.util.*;

import static me.gonkas.attexdev.util.Strings.convertStringListToPlayerArrayList;

public class GroupChat {

    private static HashMap<String, GroupChat> GROUPCHATS = new HashMap<>(); // key -> group chat
    private static HashMap<String, GroupChat> GROUPCHATCODES = new HashMap<>(); // code -> group chat
    private static HashMap<String, ArrayList<String>> GROUPCHATKEYS = new HashMap<>(); // name -> keys

    // ----------------------------------------------------------------------------

    private String name;
    private String key;
    private Player owner;

    private int size = 1;

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> moderators = new ArrayList<>();
    private ArrayList<Player> pending_invites = new ArrayList<>();
    private String active_code;

    private boolean[] changes = new boolean[6]; // 0 == name ; 1 == size ; 2 == owner ; 3 == players ; 4 == moderators ; 5 == pending_invites

    // ----------------------------------------------------------------------------

    public GroupChat(Player owner, String name) {
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
        Attex.PLAYERSETTINGS.get(owner).getGroupChatKeyList().add(this.getKey());
    }

    private GroupChat() {
        this.players = new ArrayList<>(0);
    }

    private GroupChat(YamlConfiguration config) {
        this.name = config.getString("name");
        this.key = config.getString("key");
        this.size = config.getInt("size");
        this.owner = Bukkit.getPlayer(config.getObject("owner", UUID.class));
        this.players.addAll(convertStringListToPlayerArrayList(config.getStringList("members")));
        this.players.addAll(convertStringListToPlayerArrayList(config.getStringList("moderators")));
        this.moderators.addAll(convertStringListToPlayerArrayList(config.getStringList("moderators")));
        this.pending_invites.addAll(convertStringListToPlayerArrayList(config.getStringList("invited-players")));

        for (Player player : this.players) {Attex.PLAYERSETTINGS.get(player).getGroupChatKeyList().add(key);}
    }

    // ----------------------------------------------------------------------------

    public static void loadGroupChat(YamlConfiguration config) {
        String key = config.getString("key");
        GROUPCHATS.put(key, new GroupChat(config));

        String name = config.getString("name");
        if (GROUPCHATKEYS.containsKey(name)) {GROUPCHATKEYS.get(name).add(key);}
        else {GROUPCHATKEYS.put(name, new ArrayList<>(List.of(new String[]{key})));}
    }

    public static void unloadGroupChat(String key) throws IOException {
        GroupChat gc = getGroupChatWithKey(key);
        GROUPCHATS.remove(key);
        GROUPCHATCODES.remove(gc.getCode());
        GROUPCHATKEYS.get(gc.getName()).remove(key);
        for (Player player : gc.players) {Attex.PLAYERSETTINGS.get(player).getGroupChatKeyList().remove(gc.getKey());}
        DataManagement.saveGroupChatFile(gc);
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

    // ----------------------------------------------------------------------------

    public String getName() {return this.name;}
    public String getKey() {return this.key;}
    public Player getOwner() {return this.owner;}
    public int getSize() {return this.size;}
    public ArrayList<Player> getPlayers() {return this.players;}
    public ArrayList<Player> getModerators() {return this.moderators;}
    public ArrayList<Player> getPendingInvitees() {return this.pending_invites;}
    public String getCode() {return this.active_code;}
    public boolean[] getChanges() {return this.changes;}
    public boolean isGhost() {return (this.key == null);}

    public ArrayList<String> getPlayerNames() {
        ArrayList<String> names = new ArrayList<>(0);
        for (Player player : this.players) {names.add(player.getName());}
        return names;
    }

    public ArrayList<String> getMembersNames() {
        ArrayList<String> names = new ArrayList<>(0);
        for (Player player : this.players) {
            if (!this.moderators.contains(player) || player == this.owner) {names.add(player.getName());}
        } return names;
    }
    public ArrayList<UUID> getMembersUUIDs() {
        ArrayList<UUID> uuids = new ArrayList<>(0);
        for (Player player : this.players) {
            if (!this.moderators.contains(player)) {uuids.add(player.getUniqueId());}
        } return uuids;
    }

    public ArrayList<String> getModeratorNames() {
        ArrayList<String> names = new ArrayList<>(0);
        for (Player player : this.moderators) {names.add(player.getName());}
        return names;
    }
    public ArrayList<UUID> getModeratorsUUIDs() {
        ArrayList<UUID> uuids = new ArrayList<>(0);
        for (Player player : this.moderators) {
            uuids.add(player.getUniqueId());
        } return uuids;
    }

    public ArrayList<UUID> getPendingInviteesUUIDs() {
        ArrayList<UUID> uuids = new ArrayList<>(0);
        for (Player player : this.pending_invites) {
            uuids.add(player.getUniqueId());
        } return uuids;
    }

    public HashMap<String, Boolean> getPromotable() {
        HashMap<String, Boolean> map = new HashMap<>();
        for (String mod : getModeratorNames()) {map.put(mod, false);}
        for (String mem : getMembersNames()) {map.put(mem, true);}
        return map;
    }

    public ArrayList<String> getPromotableNames() {
        return (ArrayList<String>) getPromotable().keySet().stream().toList();
    }

    // ----------------------------------------------------------------------------

    public void rename(String name) {
        this.name = name;
        this.changes[0] = true;
    }

    public void addPlayer(Player player) throws SizeLimitExceededException {
        if (this.size < 50) {
            this.players.add(player);
            this.size++;
            this.changes[3] = true;
            this.changes[1] = true;
        } else {throw new SizeLimitExceededException();}
    }

    public void removePlayer(Player player) {
        Attex.PLAYERSETTINGS.get(player).getGroupChatKeyList().remove(this.key);
        this.players.remove(player);
        this.size--;
        this.changes[3] = true;
        this.changes[1] = true;
    }

    public void invitePlayer(Player player, Player invited) {
        if (this.pending_invites.size() < 10) {
            this.pending_invites.add(invited);
            PlayerSendChat.GCInvite(invited, this.name);
        } else {this.info(player, "You have reached the pending invites limit!");}
        Attex.PLAYERSETTINGS.get(invited).getGroupChatInvitesKeyList().add(this.getKey());
        this.changes[5] = true;
    }

    public void removeInvite(Player player) {this.pending_invites.remove(player);}
    public void setOwner(Player player) {
        this.owner = player;
        this.changes[2] = true;
    }
    public void addModerator(Player player) {
        this.moderators.add(player);
        this.changes[4] = true;
    }
    public void removeModerator(Player player) {
        this.moderators.remove(player);
        this.changes[4] = true;
    }

    public void delete() {
        for (Player player : this.players) {Attex.PLAYERSETTINGS.get(player).getGroupChatKeyList().remove(this.key);}
        for (Player player : this.pending_invites) {Attex.PLAYERSETTINGS.get(player).getGroupChatInvitesKeyList().remove(this.key);}
        GROUPCHATCODES.remove(this.active_code, this);
        GROUPCHATKEYS.remove(this.name);
        GROUPCHATS.remove(this.key);
    }

    public String generateInviteCode() {
        char[] characters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuilder code = new StringBuilder();
        for (int num : new Random().ints(8, 0, 35).toArray()) {code.append(characters[num]);}

        String result = code.toString();
        if (GROUPCHATCODES.containsKey(result)) {return generateInviteCode();}

        GROUPCHATCODES.put(result, this);
        Attex.GROUPCHATCODES.put(result, (short) 0);
        this.active_code = result;
        return result;
    }

    public static GroupChat useCode(Player player, String code) throws SizeLimitExceededException {
        if (GROUPCHATCODES.containsKey(code)) {
            GROUPCHATCODES.get(code).addPlayer(player);
            GROUPCHATCODES.get(code).joinAnnouncement(player);
            return GROUPCHATCODES.get(code);
        } else {
            PlayerSendChat.GCInvalidInvite(player);
            return new GroupChat();
        }
    }

    public static void deleteCode(String code) {
        codeDeletionAnnouncement(GROUPCHATCODES.get(code));
        GROUPCHATCODES.get(code).active_code = null;
        GROUPCHATCODES.remove(code);
    }

    // ----------------------------------------------------------------------------

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
    public void kickAnnouncement(Player player) {
        this.info("Player §b" + player.getName() + "§f was kicked from this group chat!");
    }
    public void promotionAnnouncement(Player player, boolean bool) {
        if (bool) {this.info("Player §b" + player.getName() + "§f has become the owner of this group chat!");}
        else {this.info("Player §b" + player.getName() + "§f has become a moderator of this group chat!");}
    }
    public void inviteCodeAnnouncement(Player player) {
        this.info("A new invite code §b" + this.active_code + "§f has been generated by §b" + player.getName() + "§f.\n It will be active for 5 minutes.");
    }
    public void inviteAnnouncement(Player player, Player invited) {
        this.info("§b" + invited.getName() + "§f has been invited to this Group Chat by §b" + player.getName() + "§f.");
    }
    public void renameAnnouncement() {
        this.info("This group chat has been renamed to §b" + this.name + "§f!");
    }
    public static void codeDeletionAnnouncement(GroupChat gc) {
        for (Player player : gc.players) {
            player.sendMessage("§7[§bGC§7\\§9" + gc.name + "§7]§f This group chat's code has expired! Make a new one using §c\"/gc code\"§f!");
        }
    }
}