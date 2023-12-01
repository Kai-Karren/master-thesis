package de.kaikarren.complaints.survey.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@AllArgsConstructor
public class SurveyResults {

    // General questions
    @JsonProperty("lives_in_wadgassen")
    private String livesInWadgassen;

    @JsonProperty("age")
    private int age;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("chatbot_experience")
    private int chatbotExperience;


    //CUQ
    @JsonProperty("cuq_personality")
    private int cuqPersonality;

    @JsonProperty("cuq_robotic")
    private int cuqRobotic;

    @JsonProperty("cuq_welcoming")
    private int cuqWelcoming;

    @JsonProperty("cuq_unfriendly")
    private int cuqUnfriendly;

    @JsonProperty("cuq_scope")
    private int cuqScope;

    @JsonProperty("cuq_no_indication")
    private int cuqNoIndication;

    @JsonProperty("cuq_easy_navigation")
    private int cuqEasyNavigation;

    @JsonProperty("cuq_confusing")
    private int cuqConfusing;

    @JsonProperty("cuq_understanding")
    private int cuqUnderstanding;

    @JsonProperty("cuq_not_recognized")
    private int cuqNotRecognized;

    @JsonProperty("cuq_relevant_and_useful_responses")
    private int cuqRelevantAndUsefulResponses;

    @JsonProperty("cuq_irrelevant")
    private int cuqIrrelevant;

    @JsonProperty("cuq_coped_errors")
    private int cuqCopedErrors;

    @JsonProperty("cuq_unable_to_cope_errors")
    private int cuqUnableToCopeErrors;

    @JsonProperty("cuq_easy_to_use")
    private int cuqEasyToUse;

    @JsonProperty("cuq_complexity")
    private int cuqComplexity;


    // Attrack Diff Mini
    @JsonProperty("ad_s_01")
    private int ads01;

    @JsonProperty("ad_s_02")
    private int ads02;

    @JsonProperty("ad_s_03")
    private int ads03;

    @JsonProperty("ad_s_04")
    private int ads04;

    @JsonProperty("ad_s_05")
    private int ads05;

    @JsonProperty("ad_s_06")
    private int ads06;

    @JsonProperty("ad_s_07")
    private int ads07;

    @JsonProperty("ad_s_08")
    private int ads08;

    @JsonProperty("ad_s_09")
    private int ads09;

    @JsonProperty("ad_s_10")
    private int ads10;


    // Trust
    @JsonProperty("trust_1")
    private int trust1;

    @JsonProperty("trust_2")
    private int trust2;

    @JsonProperty("trust_3")
    private int trust3;

    @JsonProperty("trust_4")
    private int trust4;

    @JsonProperty("trust_5")
    private int trust5;


    // Custom questions

    @JsonProperty("custom_positive_effect_of_ai")
    private int customPositiveEffectOfAI;

    @JsonProperty("custom_experience_with_llms")
    private String customExperienceWithLLMs;

    @JsonProperty("attention_question")
    private int attentionQuestion;

    @JsonProperty("custom_my_municipality_takes_complaints_seriously")
    private int customMyMunicipalityTakesComplaintsSeriously;

    @JsonProperty("custom_complaint_assistance_willingness_to_use_it")
    private int customComplaintAssistanceWillingnessToUseIt;

    @JsonProperty("custom_human_intervention_option")
    private int customHumanInterventionOption;

    @JsonProperty("custom_desired_information")
    private String customDesiredInformation;

    @JsonProperty("custom_unwanted_information")
    private String customUnwantedInformation;

    @JsonProperty("custom_preferred_interaction_channel")
    private String customPreferredInteractionChannel;

    @JsonProperty("custom_progress_updates")
    private int customProgressUpdates;

    @JsonProperty("custom_resolution_notification")
    private int customResolutionNotification;

    @JsonProperty("custom_additional_comments")
    private String customAdditionalComments;


}
