package me.gonkas.attexdev.data;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class PlayerStats {

    ArrayList<Integer> kills;
    ArrayList<Integer> deaths;
    ArrayList<Float> killdeathratios;
    ArrayList<Short> matches;
    ArrayList<Short> wins;
    ArrayList<Short> losses;
    ArrayList<Float> winlossratios;
    ArrayList<Short> draws;
    ArrayList<Short> winstreak;
    ArrayList<Short> highest_winstreak;
    ArrayList<Integer> playtime;

    // kit IDs
    // 0 == axe; 1 == axe fp1; 2 == axe iron; 3 == op; 4 == op iron; 5 == uhc; 6 == uhc w/o cobwebs; 7 == civ kit (based on NS4);
    // 8 == crystal; 9 == dia pot; 10 == neth pot; 11 == bow

    public PlayerStats(YamlConfiguration config) {
        kills = (ArrayList<Integer>) config.getIntegerList("kills");
        deaths = (ArrayList<Integer>) config.getIntegerList("deaths");
        matches = (ArrayList<Short>) config.getShortList("matches");
        wins = (ArrayList<Short>) config.getShortList("wins");
        losses = (ArrayList<Short>) config.getShortList("losses");
        draws = (ArrayList<Short>) config.getShortList("draws");
        winstreak = (ArrayList<Short>) config.getShortList("winstreak");
        highest_winstreak = (ArrayList<Short>) config.getShortList("highest_winstreak");
        playtime = (ArrayList<Integer>) config.getIntegerList("playtime");

        killdeathratios = calculateKDR(kills, deaths);
        winlossratios = calculateWLR(wins, losses);
    }

    private static ArrayList<Float> calculateKDR(ArrayList<Integer> kills, ArrayList<Integer> deaths) {
        ArrayList<Float> killdeathratios = new ArrayList<>(0);
        for (int i=0; i < kills.size(); i++) {
            killdeathratios.add(
                    ((float) Math.floorDiv(kills.get(i) * 100, deaths.get(i))) / 100
            );
        } return killdeathratios;
    }

    private static ArrayList<Float> calculateWLR(ArrayList<Short> wins, ArrayList<Short> losses) {
        ArrayList<Float> winlossratios = new ArrayList<>(0);
        for (int i=0; i < wins.size(); i++) {
            winlossratios.add(
                    ((float) Math.floorDiv(wins.get(i) * 100, losses.get(i))) / 100
            );
        } return winlossratios;
    }

    // TO DO MORE
}