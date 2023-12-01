package de.kaikarren.complaints.feedback.api.controllers;

import de.kaikarren.complaints.data.Complaint;
import de.kaikarren.complaints.feedback.mock.MockedFeedback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FeedbackController {

    @Autowired
    private MockedFeedback feedbackGenerator;

//    @Autowired
//    private RuleBasedFeedbackGenerator feedbackGenerator;

    @PostMapping("/complaints/feedback")
    public FeedbackResponse handleFeedbackRequest(@RequestBody Complaint request){

        log.info(request.toString());

        // check if a complaint about the same problem already exists

        // if no reason is provided, try to estimate the reason
        // by first determining the nearby streets
        // then use the street names to query the reasons DB
        // if a possible reason has been found return the reason and the end data if given

        // calculate the severity

        var feedback = feedbackGenerator.generate(request);

        log.info(feedback.getText());

        return feedback;

    }

}
