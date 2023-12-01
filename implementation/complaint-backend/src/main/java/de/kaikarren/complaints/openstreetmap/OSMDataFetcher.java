package de.kaikarren.complaints.openstreetmap;

import de.kaikarren.complaints.data.GPSCoords;
import de.kaikarren.complaints.data.Location;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class OSMDataFetcher {

    public JSONObject findStreetsNearAddress(int distance, Location location){

        var coords = fetchCoordsForAddress(location);

        return findStreetsNearLocation(distance, coords.getLat(), coords.getLon());

    }

    public JSONObject findStreetsNearLocation(int distance, double lat, double lon){

        try {

            var templateFromResource = new String(getClass()
                    .getClassLoader()
                    .getResourceAsStream("osm/streets_near_location.txt").readAllBytes());

            var requestBody = String.format(
                    Locale.US,
                    templateFromResource,
                    distance,
                    lat,
                    lon
            );

            var client = HttpClient.newHttpClient();

            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://overpass-api.de/api/interpreter"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

            return new JSONObject(response.body());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return new JSONObject();
    }

    public GPSCoords fetchCoordsForAddress(Location location){

        try {

            var templateFromResource = new String(getClass()
                    .getClassLoader()
                    .getResourceAsStream("osm/coords_for_address.txt").readAllBytes());

            var requestBody = String.format(
                    Locale.US,
                    templateFromResource,
                    location.getStreet(),
                    location.getNumber(),
                    location.getCity()
            );

            var client = HttpClient.newHttpClient();

            var request = HttpRequest.newBuilder()
                    .uri(URI.create("https://overpass-api.de/api/interpreter"))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());

            return extractGPSCoordsFromResponse(new JSONObject(response.body()));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public GPSCoords extractGPSCoordsFromResponse(JSONObject response){

        var elementArray = response.getJSONArray("elements");

        if(!response.has("elements")){
            log.info(String.valueOf(response));
            return null;
        }

        if (elementArray.length() > 2){

            var node = elementArray.getJSONObject(1);

            var lat = node.getDouble("lat");
            var lon = node.getDouble("lon");

            return new GPSCoords(lat, lon);

        }

        return null;

    }

    public List<String> extractStreetsFromResponse(JSONObject response){

        var streets = new ArrayList<String>();

        if(!response.has("elements")){
            log.info(String.valueOf(response));
            return new ArrayList<>();
        }

        var elementArray = response.getJSONArray("elements");

        for(var i = 0; i < elementArray.length(); i++){

            var element = elementArray.getJSONObject(i);

            if (element.getString("type").equals("way")){

                var tags = element.getJSONObject("tags");

                if (tags.has("name")) {

                    var streetName = tags.getString("name");

                    if (!streets.contains(streetName)) {
                        streets.add(streetName);
                    }

                }

            }

        }

        return streets;

    }

}
