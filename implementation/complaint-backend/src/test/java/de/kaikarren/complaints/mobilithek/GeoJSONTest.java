package de.kaikarren.complaints.mobilithek;

import de.kaikarren.complaints.data.GPSCoords;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GeoJSONTest {

    @Test
    void getFeaturesForMockedCologneData() throws IOException, GeoJSONException {

        var dataProvider = new MockDataProvider();

        var mockedData = dataProvider.getVerkehrsKalenderKoeln();

        var geoJSON = new GeoJSON(mockedData);

        var features = geoJSON.getFeatures();

        assertEquals(129, features.length());

    }

    @Test
    void getFeatureByName() {
    }

    @Test
    void getFeatureIfNameContains() {
    }

    @Test
    void getFeaturesIfNameContains() {
    }

    @Test
    void getFeaturesNearCoords() throws IOException, GeoJSONException {

        var dataProvider = new MockDataProvider();

        var mockedData = dataProvider.getVerkehrsKalenderKoeln();

        var geoJSON = new GeoJSON(mockedData);

        var collageCenterCoords = new GPSCoords(50.930315, 6.946684);

        var features = geoJSON.getFeaturesNearCoords(collageCenterCoords, 2);

        assertEquals(57, features.size());

    }
}