package de.kaikarren.complaints.feedback.mock;

import com.google.gson.Gson;
import de.kaikarren.complaints.data.Complaint;
import de.kaikarren.complaints.data.Location;
import de.kaikarren.complaints.feedback.FeedbackGenerator;
import de.kaikarren.complaints.feedback.LevenshteinDistance;
import de.kaikarren.complaints.feedback.api.controllers.FeedbackResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class MockedFeedback implements FeedbackGenerator {

    @Override
    public FeedbackResponse generate(Complaint complaint) {

        var scenario = determineScenarioNameForComplaint(complaint);

        var fileName = String.format("feedback/%s.txt", scenario);

        log.info(fileName);

        try {
            return loadFeedbackResponseFromFile(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String determineScenarioNameForComplaint(Complaint complaint){

        var category = complaint.getCategory();

        var location = complaint.getLocation();

        if (category.equals(Complaint.Category.POLLUTION)){

            if (streetNameIsNotNullOrEmpty(location)
                    && isExpectedStreetName(List.of("Lindenstraße", "Lindenstr"),
                    location.getStreet())){

                return "pollution/pollution_b";

            } else if (streetNameIsNotNullOrEmpty(location)
                    && isExpectedStreetName(List.of("Butterborn", "butterborn"),
                    location.getStreet())) {

                return "pollution/pollution_a";

            }

        }

        if (category.equals(Complaint.Category.NOISE)){

            if (streetNameIsNotNullOrEmpty(location)
                    && isExpectedStreetName(List.of("Wendelstraße", "Wendelstr"),
                    location.getStreet())
                    && Objects.equals(location.getNumber(), "56")){

                return "noise_scenario_1/noise_1_b";

            } else if (streetNameIsNotNullOrEmpty(location)
                    && isExpectedStreetName(List.of("Brückenstraße", "Brückenstr"), location.getStreet())
                    && Objects.equals(location.getNumber(), "4")) {

                return "noise_scenario_1/noise_1_c";

            } else if (pointOfInterestIsNotEmpty(location) && location.getPointOfInterest().equalsIgnoreCase("Parkbad")){

                return "noise_scenario_2/noise_2_b";

            } else if (pointOfInterestIsNotEmpty(location) && location.getPointOfInterest().equalsIgnoreCase("Rathaus")){

                return "noise_scenario_2/noise_2_c";

            }

        } else if(category.equals(Complaint.Category.INFRASTRUCTURE)){

            if (pointOfInterestIsNotEmpty(location) && location.getPointOfInterest().equals("Kaffee Floral")){
                return "infrastructure_scenario1";
            } else if (isExpectedStreetName(List.of("Glockenstraße", "Glockenstr"), location.getStreet())){
                return "infrastructure_scenario2";
            }

        } else if (category.equals(Complaint.Category.POLLUTION)) {

            if (streetNameIsNotNullOrEmpty(location)
                    && pointOfInterestIsNotEmpty(location)
                    && location.getPointOfInterest().contains("Spielplatz")){
                return "pollution_scenario1";
            }

        }

        return "static";

    }

    public FeedbackResponse loadFeedbackResponseFromFile(String filename) throws IOException {

        try (var resourceAsStream = getClass().getClassLoader().getResourceAsStream(filename)) {

            assert resourceAsStream != null;
            var fileContent = new String(resourceAsStream.readAllBytes());

            return new Gson().fromJson(fileContent, FeedbackResponse.class);

//            return new FeedbackResponse(fileContent);

        }

    }

    private boolean streetNameIsNotNullOrEmpty(Location location) {

        return location.getStreet() != null && !location.getStreet().isEmpty();

    }

    private boolean pointOfInterestIsNotEmpty(Location location){
        return location.getPointOfInterest() != null;
    }

    private boolean isExpectedStreetName(List<String> allowedStreetNameVariants, String receivedStreetName) {

        for (String allowedStreetNameVariant : allowedStreetNameVariants) {

            if (allowedStreetNameVariant.equalsIgnoreCase(receivedStreetName)){
                return true;
            }

            if ((double) LevenshteinDistance.compare(allowedStreetNameVariant, receivedStreetName) / Math.max(allowedStreetNameVariant.length(), receivedStreetName.length()) < 0.35) {
                return true;
            }

        }

        return false;

    }

}
