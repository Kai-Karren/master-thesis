package de.kaikarren.complaints.feedback;

import de.kaikarren.complaints.data.Complaint;
import de.kaikarren.complaints.feedback.api.controllers.FeedbackResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatGPTFeedbackGenerator implements FeedbackGenerator{

    @Override
    public FeedbackResponse generate(Complaint complaint) {
        return new FeedbackResponse("");
    }

}
