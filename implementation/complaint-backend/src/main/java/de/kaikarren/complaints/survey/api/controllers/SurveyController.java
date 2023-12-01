package de.kaikarren.complaints.survey.api.controllers;

import com.fasterxml.jackson.core.JacksonException;
import com.google.gson.Gson;
import de.kaikarren.complaints.data.DBConnector;
import de.kaikarren.complaints.survey.api.data.SurveyResultsRequest;
import de.kaikarren.complaints.survey.api.data.SurveyResultsResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class SurveyController {

    @Autowired
    private DBConnector connector;

    @Autowired
    private HttpServletRequest request;

    @CrossOrigin(origins = {"http://localhost:8080", "<URL_1>", "<URL_2>"})
    @PostMapping("/survey")
    public SurveyResultsResponse handleSurveyRequest(@RequestBody SurveyResultsRequest request) throws ConversationNotFoundException {

        log.info(request.toString());

        var conversationExists = checkIfConversationExists(request.getId());

        if (conversationExists) {

            var ipAddress = getIPAddress();

            try {
                connector.addSurveyResultToDatabase(ipAddress, request);
            } catch (SQLException | JacksonException e) {
                log.error("An exception occurred while trying to store the survey results in the DB", e);
            }

            return new SurveyResultsResponse(request.getId());

        } else {

            log.error("No conversation with the id " + request.getId() + " exists.");

            log.error("Not inserted survey results " + request);

            return new SurveyResultsResponse("-1");

        }
    }

    private String getIPAddress(){

        var ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    void onConversationNotFoundException(ConversationNotFoundException exception) {}

    @CrossOrigin(origins = {"http://localhost:8080", "<URL_1>", "<URL_2>"})
    @GetMapping("/survey")
    public HashMap<String, Object> handleGetSurvey() {

        var surveyAsInputStream = getClass().getClassLoader().getResourceAsStream("surveys/primary.json");

        var surveyAsString = new BufferedReader(new InputStreamReader(surveyAsInputStream))
                .lines().collect(Collectors.joining("\n"));

        return new Gson().fromJson(surveyAsString, HashMap.class);

    }

    private boolean checkIfConversationExists(String conversationId) {

        try {

            var uri = new URI(System.getenv("CHATWOOT_BASE_URL") + conversationId);

            var request = HttpRequest.newBuilder().uri(uri)
                    .timeout(Duration.ofSeconds(10))
                    .header("api_access_token", System.getenv("CHATWOOT_ACCESS_TOKEN"))
                    .build();

            var client = HttpClient.newBuilder().build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (Exception e){
            log.error("It could not be checked if a conversation with this ID exists", e);
        }

        return false;

    }



    static class ConversationNotFoundException extends Exception {

        public ConversationNotFoundException() {
        }

        public ConversationNotFoundException(String message) {
            super(message);
        }

        public ConversationNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public ConversationNotFoundException(Throwable cause) {
            super(cause);
        }

        public ConversationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


}
