package de.kaikarren.complaints.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void parseLocationFromJson_givenStreetName() {

        var locationAsJsonString = "{\"street\":\"Adenauerstraße\"}";

        var location = Location.parseLocationFromJson(locationAsJsonString);

        assertEquals("Adenauerstraße", location.getStreet());

    }

    @Test
    void parseLocationFromJson_givenAllInformation() {

        var locationAsJsonString = """
                {
                    "street": "Adenauerstraße",
                    "number": 42,
                    "city": "Mandelbachtal",
                    "country": "DE",
                    "postalCode": "66399",
                    "coords": {
                        "lat": 8.9,
                        "lon": 7.8
                    }
                }
                """;

        var location = Location.parseLocationFromJson(locationAsJsonString);

        assertEquals("Adenauerstraße", location.getStreet());
        assertEquals("42", location.getNumber());
        assertEquals("DE", location.getCountry());
        assertEquals("66399", location.getPostalCode());
        assertEquals(8.9, location.getCoords().getLat(), 0.01);
        assertEquals(7.8, location.getCoords().getLon(), 0.01);

    }

    @Test
    void parseLocationFromJson_givenEmptyString() {

        var locationAsJsonString = "";

        var location = Location.parseLocationFromJson(locationAsJsonString);

        assertNull(location.getStreet());
        assertNull(location.getCity());
        assertNull(location.getPostalCode());
        assertNull(location.getCoords());

    }

}