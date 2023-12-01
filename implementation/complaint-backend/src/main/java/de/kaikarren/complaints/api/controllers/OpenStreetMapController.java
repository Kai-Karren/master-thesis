package de.kaikarren.complaints.api.controllers;

import de.kaikarren.complaints.api.requests.StreetsRequest;
import de.kaikarren.complaints.data.GPSCoords;
import de.kaikarren.complaints.data.Location;
import de.kaikarren.complaints.openstreetmap.OSMDataFetcher;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenStreetMapController {

    @Autowired
    private OSMDataFetcher dataFetcher;

    @PostMapping("/search/streets")
    public String getStreetsNearAddress(@RequestBody StreetsRequest request){

        var response = dataFetcher.findStreetsNearAddress(
                request.getDistance(),
                request.getLocation()
        );

        return String.format("{\"streets\":%s}", new JSONArray(dataFetcher.extractStreetsFromResponse(response)));

    }

    @PostMapping("/convert/address/coords")
    public GPSCoords getCoordsForAddress(@RequestBody Location location){

        var response = dataFetcher.fetchCoordsForAddress(location);

        return response;

    }

}
