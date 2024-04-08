package me.gonkas.attexdev.data;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class PlayerStats {

    ArrayList<Integer> playtime;
    ArrayList<Integer> kills;
    ArrayList<Integer> deaths;
    ArrayList<Float> killdeathratios;
    ArrayList<Integer> matches;
    ArrayList<Integer> wins;
    ArrayList<Integer> losses;
    ArrayList<Float> winlossratios;
    ArrayList<Integer> draws;
    ArrayList<Integer> winstreak;
    ArrayList<Integer> highest_winstreak;
    int total_winstreak;
    int total_highest_winstreak;

    // kit IDs
    // 0 == custom; 1 == axe; 2 == axe fp1; 3 == axe iron; 4 == sword; 5 == sword fp1; 6 == sword iron; 7 == uhc;
    // 8 == uhc w/o cobwebs; 9 == civ kit (based on NS4); 10 == crystal; 11 == dia pot; 12 == neth pot; 13 == bow

    public PlayerStats(YamlConfiguration config) {
        playtime = (ArrayList<Integer>) config.getIntegerList("playtime");
        kills = (ArrayList<Integer>) config.getIntegerList("kills");
        deaths = (ArrayList<Integer>) config.getIntegerList("deaths");
        matches = (ArrayList<Integer>) config.getIntegerList("matches");
        wins = (ArrayList<Integer>) config.getIntegerList("wins");
        losses = (ArrayList<Integer>) config.getIntegerList("losses");
        draws = (ArrayList<Integer>) config.getIntegerList("draws");
        winstreak = (ArrayList<Integer>) config.getIntegerList("winstreak");
        highest_winstreak = (ArrayList<Integer>) config.getIntegerList("highest_winstreak");
        total_winstreak = config.getInt("total_winstreak");
        total_highest_winstreak = config.getInt("total_highest_winstreak");

        killdeathratios = calculateRatios(kills, deaths);
        winlossratios = calculateRatios(wins, losses);
    }

    private static ArrayList<Float> calculateRatios(ArrayList<Integer> numerator, ArrayList<Integer> denominator) {
        ArrayList<Float> ratios = new ArrayList<>(0);
        for (int i=0; i < numerator.size(); i++) {
            ratios.add(
                    ((float) Math.floorDiv(numerator.get(i) * 100, denominator.get(i))) / 100
            );
        } return ratios;
    }

    public int getTotalPlaytime() {
        int total = 0;
        for (int kill : playtime) {total += kill;}
        return total;
    }
    public int getCustomPlaytime() {return playtime.get(0);}
    public int getAxePlaytime() {return playtime.get(1);}
    public int getAxeFPOnePlaytime() {return playtime.get(2);}
    public int getAxeIronPlaytime() {return playtime.get(3);}
    public int getSwordPlaytime() {return playtime.get(4);}
    public int getSwordFPOnePlaytime() {return playtime.get(5);}
    public int getSwordIronPlaytime() {return playtime.get(6);}
    public int getUHCPlaytime() {return playtime.get(7);}
    public int getUHCNoWebsPlaytime() {return playtime.get(8);}
    public int getCivPlaytime() {return playtime.get(9);}
    public int getCrystalPlaytime() {return playtime.get(10);}
    public int getDiaPotPlaytime() {return playtime.get(11);}
    public int getNethPotPlaytime() {return playtime.get(12);}
    public int getBowPlaytime() {return playtime.get(13);}

    public int getTotalKills() {
        int total = 0;
        for (int kill : kills) {total += kill;}
        return total;
    }
    public int getCustomKills() {return kills.get(0);}
    public int getAxeKills() {return kills.get(1);}
    public int getAxeFPOneKills() {return kills.get(2);}
    public int getAxeIronKills() {return kills.get(3);}
    public int getSwordKills() {return kills.get(4);}
    public int getSwordFPOneKills() {return kills.get(5);}
    public int getSwordIronKills() {return kills.get(6);}
    public int getUHCKills() {return kills.get(7);}
    public int getUHCNoWebsKills() {return kills.get(8);}
    public int getCivKills() {return kills.get(9);}
    public int getCrystalKills() {return kills.get(10);}
    public int getDiaPotKills() {return kills.get(11);}
    public int getNethPotKills() {return kills.get(12);}
    public int getBowKills() {return kills.get(13);}

    public int getTotalDeaths() {
        int total = 0;
        for (int death : deaths) {total += death;}
        return total;
    }
    public int getCustomDeaths() {return deaths.get(0);}
    public int getAxeDeaths() {return deaths.get(1);}
    public int getAxeFPOneDeaths() {return deaths.get(2);}
    public int getAxeIronDeaths() {return deaths.get(3);}
    public int getSwordDeaths() {return deaths.get(4);}
    public int getSwordFPOneDeaths() {return deaths.get(5);}
    public int getSwordIronDeaths() {return deaths.get(6);}
    public int getUHCDeaths() {return deaths.get(7);}
    public int getUHCNoWebsDeaths() {return deaths.get(8);}
    public int getCivDeaths() {return deaths.get(9);}
    public int getCrystalDeaths() {return deaths.get(10);}
    public int getDiaPotDeaths() {return deaths.get(11);}
    public int getNethPotDeaths() {return deaths.get(12);}
    public int getBowDeaths() {return deaths.get(13);}

    public float getTotalKDR() {return ((float) Math.floorDiv(getTotalKills() * 100, getTotalDeaths())) / 100;}
    public float getCustomKDR() {return killdeathratios.get(0);}
    public float getAxeKDR() {return killdeathratios.get(1);}
    public float getAxeFPOneKDR() {return killdeathratios.get(2);}
    public float getAxeIronKDR() {return killdeathratios.get(3);}
    public float getSwordKDR() {return killdeathratios.get(4);}
    public float getSwordFPOneKDR() {return killdeathratios.get(5);}
    public float getSwordIronKDR() {return killdeathratios.get(6);}
    public float getUHCKDR() {return killdeathratios.get(7);}
    public float getUHCNoWebsKDR() {return killdeathratios.get(8);}
    public float getCivKDR() {return killdeathratios.get(9);}
    public float getCrystalKDR() {return killdeathratios.get(10);}
    public float getDiaPotKDR() {return killdeathratios.get(11);}
    public float getNethPotKDR() {return killdeathratios.get(12);}
    public float getBowKDR() {return killdeathratios.get(13);}

    public int getTotalMatches() {
        int total = 0;
        for (int num : matches) {total += num;}
        return total;
    }
    public int getCustomMatches() {return matches.get(0);}
    public int getAxeMatches() {return matches.get(1);}
    public int getAxeFPOneMatches() {return matches.get(2);}
    public int getAxeIronMatches() {return matches.get(3);}
    public int getSwordMatches() {return matches.get(4);}
    public int getSwordFPOneMatches() {return matches.get(5);}
    public int getSwordIronMatches() {return matches.get(6);}
    public int getUHCMatches() {return matches.get(7);}
    public int getUHCNoWebsMatches() {return matches.get(8);}
    public int getCivMatches() {return matches.get(9);}
    public int getCrystalMatches() {return matches.get(10);}
    public int getDiaPotMatches() {return matches.get(11);}
    public int getNethPotMatches() {return matches.get(12);}
    public int getBowMatches() {return matches.get(13);}

    public int getTotalWins() {
        int total = 0;
        for (int num : wins) {total += num;}
        return total;
    }
    public int getCustomWins() {return wins.get(0);}
    public int getAxeWins() {return wins.get(1);}
    public int getAxeFPOneWins() {return wins.get(2);}
    public int getAxeIronWins() {return wins.get(3);}
    public int getSwordWins() {return wins.get(4);}
    public int getSwordFPOneWins() {return wins.get(5);}
    public int getSwordIronWins() {return wins.get(6);}
    public int getUHCWins() {return wins.get(7);}
    public int getUHCNoWebsWins() {return wins.get(8);}
    public int getCivWins() {return wins.get(9);}
    public int getCrystalWins() {return wins.get(10);}
    public int getDiaPotWins() {return wins.get(11);}
    public int getNethPotWins() {return wins.get(12);}
    public int getBowWins() {return wins.get(13);}

    public int getTotalLosses() {
        int total = 0;
        for (int num : losses) {total += num;}
        return total;
    }
    public int getCustomLosses() {return losses.get(0);}
    public int getAxeLosses() {return losses.get(1);}
    public int getAxeFPOneLosses() {return losses.get(2);}
    public int getAxeIronLosses() {return losses.get(3);}
    public int getSwordLosses() {return losses.get(4);}
    public int getSwordFPOneLosses() {return losses.get(5);}
    public int getSwordIronLosses() {return losses.get(6);}
    public int getUHCLosses() {return losses.get(7);}
    public int getUHCNoWebsLosses() {return losses.get(8);}
    public int getCivLosses() {return losses.get(9);}
    public int getCrystalLosses() {return losses.get(10);}
    public int getDiaPotLosses() {return losses.get(11);}
    public int getNethPotLosses() {return losses.get(12);}
    public int getBowLosses() {return losses.get(13);}

    public int getTotalDraws() {
        int total = 0;
        for (int num : draws) {total += num;}
        return total;
    }
    public int getCustomDraws() {return draws.get(0);}
    public int getAxeDraws() {return draws.get(1);}
    public int getAxeFPOneDraws() {return draws.get(2);}
    public int getAxeIronDraws() {return draws.get(3);}
    public int getSwordDraws() {return draws.get(4);}
    public int getSwordFPOneDraws() {return draws.get(5);}
    public int getSwordIronDraws() {return draws.get(6);}
    public int getUHCDraws() {return draws.get(7);}
    public int getUHCNoWebsDraws() {return draws.get(8);}
    public int getCivDraws() {return draws.get(9);}
    public int getCrystalDraws() {return draws.get(10);}
    public int getDiaPotDraws() {return draws.get(11);}
    public int getNethPotDraws() {return draws.get(12);}
    public int getBowDraws() {return draws.get(13);}

    public int getTotalWinstreak() {return total_winstreak;}
    public int getCustomWinstreak() {return winstreak.get(0);}
    public int getAxeWinstreak() {return winstreak.get(1);}
    public int getAxeFPOneWinstreak() {return winstreak.get(2);}
    public int getAxeIronWinstreak() {return winstreak.get(3);}
    public int getSwordWinstreak() {return winstreak.get(4);}
    public int getSwordFPOneWinstreak() {return winstreak.get(5);}
    public int getSwordIronWinstreak() {return winstreak.get(6);}
    public int getUHCWinstreak() {return winstreak.get(7);}
    public int getUHCNoWebsWinstreak() {return winstreak.get(8);}
    public int getCivWinstreak() {return winstreak.get(9);}
    public int getCrystalWinstreak() {return winstreak.get(10);}
    public int getDiaPotWinstreak() {return winstreak.get(11);}
    public int getNethPotWinstreak() {return winstreak.get(12);}
    public int getBowWinstreak() {return winstreak.get(13);}

    public int getTotalHighestWinstreak() {return total_winstreak;}
    public int getCustomHighestWinstreak() {return highest_winstreak.get(0);}
    public int getAxeHighestWinstreak() {return highest_winstreak.get(1);}
    public int getAxeFPOneHighestWinstreak() {return highest_winstreak.get(2);}
    public int getAxeIronHighestWinstreak() {return highest_winstreak.get(3);}
    public int getSwordHighestWinstreak() {return highest_winstreak.get(4);}
    public int getSwordFPOneHighestWinstreak() {return highest_winstreak.get(5);}
    public int getSwordIronHighestWinstreak() {return highest_winstreak.get(6);}
    public int getUHCHighestWinstreak() {return highest_winstreak.get(7);}
    public int getUHCNoWebsHighestWinstreak() {return highest_winstreak.get(8);}
    public int getCivHighestWinstreak() {return highest_winstreak.get(9);}
    public int getCrystalHighestWinstreak() {return highest_winstreak.get(10);}
    public int getDiaPotHighestWinstreak() {return highest_winstreak.get(11);}
    public int getNethPotHighestWinstreak() {return highest_winstreak.get(12);}
    public int getBowHighestWinstreak() {return highest_winstreak.get(13);}
}