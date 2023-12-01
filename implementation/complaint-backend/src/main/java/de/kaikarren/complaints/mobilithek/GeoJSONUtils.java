package de.kaikarren.complaints.mobilithek;

import de.kaikarren.complaints.data.GPSCoords;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeoJSONUtils {

    public static GPSCoords getCoordsFromFeature(JSONObject feature){

        var geometry = feature.getJSONObject("geometry");

        var x = geometry.getDouble("x");
        var y = geometry.getDouble("y");

        return new GPSCoords(y, x);

    }

    public static List<JSONObject> filterFeaturesByType(List<JSONObject> features, List<Integer> relevantTypes){

        var filteredList = new ArrayList<JSONObject>();

        for (var feature : features){

            var type = feature.getInt("type");

            if (relevantTypes.contains(type)){
                filteredList.add(feature);
            }

        }

        return filteredList;

    }

}
