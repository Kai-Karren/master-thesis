package de.kaikarren.complaints.survey.api.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@AllArgsConstructor
public class SurveyResultsRequest {

    private String id;

    private String condition;

    private String uuid;

    private SurveyResults results;

}
