package de.kaikarren.complaints.feedback;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LevenshteinDistance {

    public static int compare(String a, String b) {

        if (a.isEmpty()) {
            return b.length();
        }

        if (b.isEmpty()) {
            return a.length();
        }

        int indicator = 0;

        if (!a.substring(0, 1).equals(b.substring(0, 1))){
            indicator = 1;
        }

        return Math.min(Math.min(compare(a.substring(1), b) +1, compare(b.substring(1), a) + 1), compare(a.substring(1), b.substring(1)) + indicator);

    }
}
