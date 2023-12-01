package de.kaikarren.complaints.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor @Getter @Setter
public class GPSCoords {

    private Double lat;
    private Double lon;

}
