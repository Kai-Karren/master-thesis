package de.kaikarren.complaints.api.requests;

import de.kaikarren.complaints.data.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class StreetsRequest {

    private int distance;
    private Location location;

}
