package me.gonkas.attexdev.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Strings {

    public static List<String> compareStrings(String input, List<String> strings) {
        ArrayList<String> candidates = new ArrayList<>(0);
        ArrayList<String> matches = new ArrayList<>(0);

        for (String s : strings) {
            try {candidates.add(s.substring(0, input.length()));}
            catch (IndexOutOfBoundsException ignored) {candidates.add(null);}
        }
        for (int i=0; i < candidates.size(); i++) {
            if (candidates.get(i) != null) {
                if (candidates.get(i).equals(input)) {matches.add(strings.get(i));}
            }
        } return matches;
    }

    public static List<String> compareStrings(String input, String[] strings) {
        ArrayList<String> candidates = new ArrayList<>(0);
        ArrayList<String> matches = new ArrayList<>(0);

        for (String s : strings) {
            try {candidates.add(s.substring(0, input.length()));}
            catch (IndexOutOfBoundsException ignored) {candidates.add(null);}
        }
        for (int i=0; i < candidates.size(); i++) {
            if (candidates.get(i) != null) {
                if (candidates.get(i).equals(input)) {matches.add(strings[i]);}
            }
        } return matches;
    }

    public static ArrayList<UUID> convertStringListToUUIDArrayList(List<String> list) {
        ArrayList<UUID> uuids = new ArrayList<>(0);
        for (String s : list) {
            uuids.add(UUID.fromString(s));
        } return uuids;
    }

    public static ArrayList<Player> convertStringListToPlayerArrayList(List<String> list) {
        ArrayList<Player> players = new ArrayList<>(0);
        for (String s : list) {
            players.add(Bukkit.getPlayer(UUID.fromString(s)));
        } return players;
    }
}
