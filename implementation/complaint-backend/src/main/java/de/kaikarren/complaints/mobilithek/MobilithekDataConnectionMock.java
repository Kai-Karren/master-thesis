package de.kaikarren.complaints.mobilithek;

import de.kaikarren.complaints.data.Complaint;
import de.kaikarren.complaints.data.ExternalDataConnection;
import de.kaikarren.complaints.openstreetmap.OSMDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MobilithekDataConnectionMock implements ExternalDataConnection {

    private final GeoJSON cologneData;
    private final double maxDistanceInKilometer = 1;

    public MobilithekDataConnectionMock(){
        cologneData = new GeoJSON(new MockDataProvider().getVerkehrsKalenderKoeln());
    }

    @Override
    public List<JSONObject> fetchData(Complaint complaint) {

        try {

            List<JSONObject> features = null;

            // First check if there are GeoJSON features in the same street or with the same POI.
            if (!complaint.getLocation().getStreet().isEmpty()){

                features = cologneData.getFeaturesIfNameContains(complaint.getLocation().getStreet());

            } else if (!complaint.getLocation().getPointOfInterest().isEmpty()) {

                features = cologneData.getFeaturesIfNameContains(complaint.getLocation().getPointOfInterest());

            }

            // If not use a distance-based filter
            if (features == null || features.isEmpty()) {

                if (complaint.getLocation().getCoords() == null){

                    var coords = new OSMDataFetcher().fetchCoordsForAddress(complaint.getLocation());

                    complaint.getLocation().setCoords(coords);
                }

                features = cologneData.getFeaturesNearCoords(complaint.getLocation().getCoords(), maxDistanceInKilometer);

            }

            features = filterData(features, complaint);

            return features;

        } catch (Exception e){
            log.error("Exception while fetching the data from the predefined GeoJSON", e);
        }

        return new ArrayList<>();
    }

    public List<JSONObject> filterData(List<JSONObject> features, Complaint complaint){

        switch (complaint.getCategory()){
            case NOISE -> {
                features = GeoJSONUtils.filterFeaturesByType(features, List.of(1));
            }
            case TRANSPORTATION -> {
                features = GeoJSONUtils.filterFeaturesByType(features, List.of(2,3));
            }
        }

        return features;

    }

}
