package de.kaikarren.complaints.api.requests;

import de.kaikarren.complaints.data.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Setter @Getter
public class SearchRequest {

    private String category;
    private Location location;
    private String text;

}
