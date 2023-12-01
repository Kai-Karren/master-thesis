package de.kaikarren.complaints.feedback.api.controllers;

import de.kaikarren.complaints.data.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class FeedbackRequest {

    private String user;

    private String reason;

    private String category;

    private Location location;

}
