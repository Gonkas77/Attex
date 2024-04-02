package me.gonkas.attex.util;

import java.util.ArrayList;
import java.util.List;

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
}
