package de.kaikarren.complaints.feedback;

import de.kaikarren.complaints.data.Complaint;
import de.kaikarren.complaints.feedback.api.controllers.FeedbackResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public interface FeedbackGenerator {

    public FeedbackResponse generate(Complaint complaint);

}
