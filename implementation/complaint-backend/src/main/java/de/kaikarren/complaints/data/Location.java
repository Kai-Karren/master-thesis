package de.kaikarren.complaints.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class Location {

    private String street;
    private String number;
    private String city;
    private String country = "DE";
    private String postalCode;
    private GPSCoords coords;
    @JsonProperty("point_of_interest")
    private String pointOfInterest;

    public Location(LocationBuilder builder) {
        this.street = builder.street;
        this.number = builder.number;
        this.city = builder.city;
        this.country = builder.country;
        this.postalCode = builder.postalCode;
        this.coords = builder.coords;
        this.pointOfInterest = builder.pointOfInterest;
    }

    public static Location parseLocationFromJson(String locationAsJsonString){

        if (locationAsJsonString == null
                || locationAsJsonString.trim().equals("")
                || locationAsJsonString.trim().equals("{}")
        ) {
            return new Location();
        } else {
            var gson = new Gson();

            return gson.fromJson(locationAsJsonString, Location.class);
        }

    }

    @Getter
    public static class LocationBuilder {

        private String street;
        private String number;
        private String city;
        private String country = "DE";
        private String postalCode;
        private GPSCoords coords;
        private String pointOfInterest;

        public LocationBuilder setStreet(String street) {
            this.street = street;
            return this;
        }

        public LocationBuilder setNumber(String number) {
            this.number = number;
            return this;
        }

        public LocationBuilder setCity(String city) {
            this.city = city;
            return this;
        }

        public LocationBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public LocationBuilder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public LocationBuilder setCoords(GPSCoords coords) {
            this.coords = coords;
            return this;
        }

        public LocationBuilder setPointOfInterest(String pointOfInterest) {
            this.pointOfInterest = pointOfInterest;
            return this;
        }

        public Location build() {
            return new Location(this);
        }

    }

}
