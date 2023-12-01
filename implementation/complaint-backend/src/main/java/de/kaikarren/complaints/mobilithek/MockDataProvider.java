package de.kaikarren.complaints.mobilithek;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

@Slf4j
public class MockDataProvider {

    public JSONObject getVerkehrsKalenderKoeln() {

        try {
            var inputStream = this.getClass().getClassLoader().getResourceAsStream("mobilithek/verkehrs_kalender_koeln.json");

            var contentAsString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            return new JSONObject(contentAsString);

        } catch (Exception e) {
            log.error("Could not read the resource file", e);
        }

        return null;

    }

}
