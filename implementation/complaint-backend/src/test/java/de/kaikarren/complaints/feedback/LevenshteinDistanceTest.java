package de.kaikarren.complaints.feedback;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevenshteinDistanceTest {

    @Test
    void compareLindenstraße() {

        var a = "Lindenstraße";

        var b = "Lndnstraße";

        var distance = LevenshteinDistance.compare(a, b);

        assertEquals(2, distance);

    }

    @Test
    void compareButterborn() {

        var a = "Butterborn";

        var b = "born";

        var distance = LevenshteinDistance.compare(a, b);

        assertEquals(6, distance);

    }

}