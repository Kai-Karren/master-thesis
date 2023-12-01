package de.kaikarren.complaints.mobilithek;

import de.kaikarren.complaints.data.GPSCoords;
import de.kaikarren.complaints.openstreetmap.OSMUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GeoJSON {

    @Getter
    private final JSONObject jsonObject;

    public GeoJSON(String geoJsonAsString){
        this.jsonObject = new JSONObject(geoJsonAsString);
    }

    public GeoJSON(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public JSONArray getFeatures() throws GeoJSONException {

        if (!this.jsonObject.has("features")){
            throw new GeoJSONException("JSON object has no features.");
        }

        return this.jsonObject.getJSONArray("features");

    }

    public JSONObject getFeatureByName(String name) throws GeoJSONException {

        var features = getFeatures();

        for (var i = 0; i < features.length(); i++){

            var feature = features.getJSONObject(i);

            if (!feature.has("name")){
                throw new GeoJSONException("Feature has no name");
            }

            if (feature.getString("name").equals(name)){
                return feature;
            }

        }

        return null;

    }

    public JSONObject getFeatureIfNameContains(String contentThatShouldBeIncluded) throws GeoJSONException {

        var features = getFeatures();

        for (var i = 0; i < features.length(); i++){

            var feature = features.getJSONObject(i);

            if (!feature.has("name")){
                throw new GeoJSONException("Feature has no name");
            }

            if (feature.getString("name").contains(contentThatShouldBeIncluded)){
                return feature;
            }

        }

        return null;

    }

    public List<JSONObject> getFeaturesIfNameContains(String contentThatShouldBeIncluded) throws GeoJSONException {

        var matchingFeatures = new ArrayList<JSONObject>();

        var features = getFeatures();

        for (var i = 0; i < features.length(); i++){

            var feature = features.getJSONObject(i);

            if (!feature.has("name")){
                throw new GeoJSONException("Feature has no name");
            }

            if (feature.getString("name").contains(contentThatShouldBeIncluded)){
                matchingFeatures.add(feature);
            }

        }

        return matchingFeatures;

    }

    public List<JSONObject> getFeaturesNearCoords(GPSCoords coords, double maxDistanceInKilometer) throws GeoJSONException {

        var matchingFeatures = new ArrayList<JSONObject>();

        var features = getFeatures();

        for (var i = 0; i < features.length(); i++){

            var feature = features.getJSONObject(i);

            var featureCoords = GeoJSONUtils.getCoordsFromFeature(feature);

            var distance = OSMUtils.distance(coords, featureCoords);

            if (distance < maxDistanceInKilometer){
                matchingFeatures.add(feature);
            }

        }

        return matchingFeatures;

    }

}
