package de.kaikarren.complaints.openstreetmap;

import de.kaikarren.complaints.data.GPSCoords;
import de.kaikarren.complaints.data.Location;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OSMDataFetcherTest {

//    @Test
    void findStreetsNearAddress() {

        var dataFetcher = new OSMDataFetcher();

        var location = new Location(
                "Adenauerstraße",
                "96",
                "Mandelbachtal",
                "DE",
                "66399",
                null,
                ""
        );

        var response = dataFetcher.findStreetsNearAddress(50, location);

        System.out.println(response);

    }

//    @Test
    void findStreetsNearLocation() {

        var dataFetcher = new OSMDataFetcher();

        var response = dataFetcher.findStreetsNearLocation(25, 47.5, 7.6);

    }

//    @Test
    void fetchCoordsForAddress() {

        var dataFetcher = new OSMDataFetcher();

        var location = new Location(
                "Adenauerstraße",
                "2",
                "Mandelbachtal",
                "DE",
                "66399",
                null,
                ""
        );

        var coords = dataFetcher.fetchCoordsForAddress(location);

        System.out.println(coords);

    }

//    @Test
    void extractGPSCoordsFromResponse() throws IOException, JSONException {

        var responseAsString = new String(getClass()
                .getClassLoader()
                .getResourceAsStream("osm/coords_for_address_response.txt").readAllBytes());

        var json = new JSONObject(responseAsString);

        var dataFetcher = new OSMDataFetcher();

        var coords = dataFetcher.extractGPSCoordsFromResponse(json);

        assertEquals(49.1973203, coords.getLat(), 0.01);
        assertEquals(7.1424319, coords.getLon(), 0.01);

    }

//    @Test
    void extractStreetsFromResponse() throws IOException, JSONException {

        var responseAsString = new String(getClass()
                .getClassLoader()
                .getResourceAsStream("osm/street_responses.txt").readAllBytes());

        var json = new JSONObject(responseAsString);

        var dataFetcher = new OSMDataFetcher();

        var streets = dataFetcher.extractStreetsFromResponse(json);

        assertEquals(2, streets.size());

    }
}