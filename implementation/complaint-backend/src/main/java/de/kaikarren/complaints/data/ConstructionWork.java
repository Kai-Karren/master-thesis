package de.kaikarren.complaints.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor @Getter @Setter
public class ConstructionWork {

    private String uuid;
    private String streetName;
    private Timestamp startDate;
    private Timestamp endDate;
    private String type;
    private String description;
    private String link;
    private String status;
    private GPSCoords coords;

}
