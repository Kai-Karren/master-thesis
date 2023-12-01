package de.kaikarren.complaints.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Feedback {

    @Getter @Setter
    private String startDate;

    @Getter @Setter
    private String endDate;
    
    @Getter @Setter
    private String reason;

    @Getter @Setter
    private String severity;

}
