package de.kaikarren.complaints.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import de.kaikarren.complaints.survey.api.data.SurveyResultsRequest;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class DBConnector {

    private static final String SURVEY_RESULTS_DB = "<HARDCODED_DB_NAME>";

    public List<Complaint> getComplaintsFilteredByTimestampAndTopicCategory(String timestamp, String category) throws SQLException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        var sql = createGetComplaintsFilteredByTimestampAndCategory(timestamp, category);

        var resultSet = executeQuery(connection, sql);

        connection.close();

        return parseComplaintsFromResultSet(resultSet);

    }

    private String createGetComplaintsFilteredByTimestampAndCategory(String timestamp, String category){
        return String.format("""
                SELECT * FROM complaints WHERE creation_date >= '%s' AND creation_date < '%s' AND category = '%s';
                """, timestamp, Timestamp.from(Instant.now()), category);
    }

    public List<Complaint> getComplaintsFilteredByTopicCategory(String category) throws SQLException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        var sql = createGetComplaintsFilteredByCategory(category);

        var resultSet = executeQuery(connection, sql);

        connection.close();

        return parseComplaintsFromResultSet(resultSet);

    }

    private String createGetComplaintsFilteredByCategory(String category){
        return String.format("""
                SELECT * FROM complaints
                WHERE category = '%s';
                """, category);
    }

    public List<Complaint> getComplaintsFilteredByTime(String timestamp) throws SQLException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        var sql = createGetComplaintsFilteredByTime(timestamp);

        var resultSet = executeQuery(connection, sql);

        connection.close();

        return parseComplaintsFromResultSet(resultSet);

    }

    private String createGetComplaintsFilteredByTime(String timestamp){
        return String.format("""
                SELECT * FROM complaints
                WHERE creation_date BETWEEN '%s' AND '%s';
                """, timestamp, Timestamp.from(Instant.now()));
    }

    public List<Complaint> getComplaints() throws SQLException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        var sql = createGetAllComplaintsQuery();

        var resultSet = executeQuery(connection, sql);

        connection.close();

        return parseComplaintsFromResultSet(resultSet);

    }

    private String createGetAllComplaintsQuery(){
        return """
                SELECT * FROM complaints;
                """;
    }

    private List<Complaint> parseComplaintsFromResultSet(ResultSet resultSet) throws SQLException {

        var complaintList = new ArrayList<Complaint>();

        while(resultSet.next()) {

            var uuid = resultSet.getString("uuid");
            var name = resultSet.getString("name");
            var summary = resultSet.getString("summary");
            var creationDate = resultSet.getTimestamp("creation_date");
            var lastUpdated = resultSet.getTimestamp("last_updated");
            var category = resultSet.getString("category");
            var status = resultSet.getString("status");
            var location = parseLocationFromResultSet(resultSet);
            var reason = resultSet.getString("reason");
            var severity = resultSet.getString("severity");

            var conversations = parseConversationsFromResultSet(resultSet);
            var users = parseUsersFromResultSet(resultSet);

            var complaintBuilder = new Complaint.ComplaintBuilder(uuid);
            complaintBuilder.setName(name)
                    .setSummary(summary)
                    .setCreationDate(creationDate.toInstant())
                    .setStatus(status)
                    .setCategory(Complaint.Category.valueOf(category))
                    .setSeverity(severity)
                    .setLocation(location)
                    .setReason(reason)
                    .setConversations(conversations)
                    .setUsers(users);

            if (lastUpdated != null){
                complaintBuilder.setLastUpdated(lastUpdated.toInstant());
            }

            var complaint = complaintBuilder.build();

            complaintList.add(complaint);
        }

        return complaintList;

    }

    public String addComplaintToDatabase(Complaint complaint) throws SQLException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        //always create a new uuid for the complaint

        var uuid = UUID.randomUUID().toString();
        complaint.setUuid(uuid);

        var sql = createComplaintInsertQuery(complaint);

        log.info(sql);

        var executedStatement = execute(connection, sql);

        connection.close();

        return uuid;

    }

    public String createComplaintInsertQuery(Complaint complaint) {

        var creationDateAsInstant = complaint.getCreationDate();

        if(creationDateAsInstant == null){
            creationDateAsInstant = Instant.now();
        }

        var creationDate = convertInstantToTimestamp(creationDateAsInstant);

        return String.format("""
                INSERT INTO complaints
                    (
                        uuid,
                        name,
                        summary,
                        category,
                        location,
                        severity
                        status,
                        conversations,
                        users,
                        creation_date,
                        last_updated
                    )
                    VALUES (
                        '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'
                    );
                """,
                complaint.getUuid(),
                complaint.getName(),
                complaint.getSummary(),
                complaint.getCategory(),
                new Gson().toJson(complaint.getLocation()),
                complaint.getSeverity(),
                complaint.getStatus(),
                new Gson().toJson(complaint.getConversations()),
                new Gson().toJson(complaint.getUsers()),
                creationDate,
                creationDate
        );
    }

    public String addSurveyResultToDatabase(String ipAddress, SurveyResultsRequest surveyResult) throws SQLException, JsonProcessingException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        var sql = createSurveyResultInsertQuery(ipAddress, surveyResult);

        log.info(sql);

        var executedStatement = execute(connection, sql);

        connection.close();

        return surveyResult.getId();

    }

    public String createSurveyResultInsertQuery(String ipAddress, SurveyResultsRequest surveyResults) throws JsonProcessingException {

        var timestamp = convertInstantToTimestamp(Instant.now());

        var objectMapper = new ObjectMapper();

        return String.format("""
                INSERT INTO %s
                    (
                        conversation_id,
                        ip,
                        results,
                        timestamp,
                        condition
                    )
                    VALUES (
                        '%s', '%s', '%s', '%s', '%s'
                    );
                """,
                SURVEY_RESULTS_DB,
                surveyResults.getId(),
                ipAddress,
                objectMapper.writeValueAsString(surveyResults.getResults()),
                timestamp,
                surveyResults.getCondition()
        );
    }

    public Complaint updateComplaintInDatabase(Complaint complaint) throws SQLException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        complaint.setLastUpdated(Instant.now());

        var sql = createComplaintUpdateQuery(complaint);

        log.info(sql);

        var executedStatement = execute(connection, sql);

        connection.close();

        return complaint;

    }

    private String createComplaintUpdateQuery(Complaint complaint) {

        var creationDateAsInstant = complaint.getCreationDate();

        if(creationDateAsInstant == null){
            creationDateAsInstant = Instant.now();
        }

        var creationDate = convertInstantToTimestamp(creationDateAsInstant);

        return String.format("""
                UPDATE complaints
                SET name = '%s',
                    summary = '%s',
                    category = '%s',
                    severity = '%s',
                    location = '%s',
                    text = '%s',
                    conversations = '%s',
                    users = '%s',
                    creation_date = '%s',
                    last_updated = '%s'
                WHERE uuid = '%s';
                """, complaint.getName(),
                complaint.getSummary(),
                complaint.getCategory(),
                complaint.getSeverity(),
                new Gson().toJson(complaint.getLocation()),
                new Gson().toJson(complaint.getConversations()),
                new Gson().toJson(complaint.getUsers()),
                creationDate,
                complaint.getLastUpdated(),
                complaint.getUuid()
        );

    }

    public Complaint getComplaintWithUuid(String uuid) throws SQLException {

        var connection = createConnection(System.getenv("JDBC_COMPLAINTS_DB_URL"));

        var sql = createComplaintSearchByUuidQuery(uuid);

        var resultSet = executeQuery(connection, sql);

        connection.close();

        return convertResultSetToComplaint(resultSet);

    }

    private String createComplaintSearchByUuidQuery(String uuid){

        return String.format("""
                SELECT * FROM complaints WHERE uuid = '%s';
                """, uuid);

    }

    private Complaint convertResultSetToComplaint(ResultSet resultSet) throws SQLException {

        if(resultSet.next()){

            var uuid = resultSet.getString("uuid");
            var name = resultSet.getString("name");
            var summary = resultSet.getString("summary");
            var creationDate = resultSet.getTimestamp("creation_date");
            var lastUpdated = resultSet.getTimestamp("last_updated");
            var category = resultSet.getString("category");
            var text = resultSet.getString("text");
            var status = resultSet.getString("status");
            var location = parseLocationFromResultSet(resultSet);
            var reason = resultSet.getString("reason");
            var severity = resultSet.getString("severity");

            var conversations = parseConversationsFromResultSet(resultSet);
            var users = parseUsersFromResultSet(resultSet);

            var complaintBuilder = new Complaint.ComplaintBuilder(uuid);
            complaintBuilder.setName(name)
                    .setSummary(summary)
                    .setCreationDate(creationDate.toInstant())
                    .setStatus(status)
                    .setCategory(Complaint.Category.valueOf(category))
                    .setSeverity(severity)
                    .setLocation(location)
                    .setReason(reason)
                    .setConversations(conversations)
                    .setUsers(users);

            if (lastUpdated != null){
                complaintBuilder.setLastUpdated(lastUpdated.toInstant());
            }

            return complaintBuilder.build();

        } else {
            return null;
        }

    }

    private Location parseLocationFromResultSet(ResultSet resultSet) throws SQLException {

        var locationAsString = resultSet.getString("location");

        return Location.parseLocationFromJson(locationAsString);

    }

    private List<Conversation> parseConversationsFromResultSet(ResultSet resultSet) throws SQLException {

        var conversationAsJsonString = resultSet.getString("conversations");

        try {

            return Arrays.asList(new Gson().fromJson(conversationAsJsonString, Conversation[].class));

        } catch (Exception e) {
            log.debug("Error while parsing the conversations", e);
            return List.of();
        }

    }

    private List<User> parseUsersFromResultSet(ResultSet resultSet) throws SQLException {

        var conversationAsJsonString = resultSet.getString("users");

        try {

            return Arrays.asList(new Gson().fromJson(conversationAsJsonString, User[].class));

        } catch (Exception e){
            log.debug("Error while parsing the users", e);
            return List.of();
        }

    }

    public List<ConstructionWork> fetchActiveConstructionWorks(Instant currentTime){

        var timestamp = convertInstantToTimestamp(currentTime);

        try {
            return fetchActiveConstructionWorksFromDB(timestamp);
        } catch (SQLException exception) {
            log.error("An exception occurred while trying to fetch" +
                    " the active construction works from the DB", exception);
        }

        return new ArrayList<>();
    }

    private Timestamp convertInstantToTimestamp(Instant instant){
        return Timestamp.from(instant);
    }

    private List<ConstructionWork> fetchActiveConstructionWorksFromDB(Timestamp currentTime) throws SQLException {

        var connection = createConnection(System.getenv("JDBC_MOCKED_DB_URL"));

        var sql = getActiveConstructionWorksSQLQuery(currentTime);

        log.info(sql);

        var results = executeQuery(connection, sql);

        connection.close();

        return convertResultSetToConstructionWorksList(results);

    }

    private List<ConstructionWork> convertResultSetToConstructionWorksList(ResultSet resultSet) throws SQLException {

        var constructionWorks = new ArrayList<ConstructionWork>();

        while (resultSet.next()){
            constructionWorks.add(convertResultSetToConstructionWork(resultSet));
        }

        return constructionWorks;

    }

    private ConstructionWork convertResultSetToConstructionWork(ResultSet resultSet) throws SQLException {

        var uuid = resultSet.getString("id");
        var streetName = resultSet.getString("name");
        var startDate = resultSet.getTimestamp("start_date");
        var endDate = resultSet.getTimestamp("end_date");
        var typ = resultSet.getString("typ");
        var description = resultSet.getString("description");
        var link = resultSet.getString("link");
        var status = resultSet.getString("status");

        //TODO
        var gpsCoordsToParse = resultSet.getString("geometry");

        return new ConstructionWork(
                uuid,
                streetName,
                startDate,
                endDate,
                typ,
                description,
                link,
                status,
                new GPSCoords(0.0, 0.0)
                );
    }

    private String getActiveConstructionWorksSQLQuery(Timestamp timestampToInsert) {
        return """
        SELECT * FROM construction_works WHERE start_date <= '{}' AND end_date >= '{}';
        """.replace("{}", timestampToInsert.toString());
    }

    private Connection createConnection(String databaseUrl) throws SQLException {

        var datasource = new PGSimpleDataSource();
        datasource.setUrl(databaseUrl);

        return datasource.getConnection();

    }

    private ResultSet executeQuery(Connection connection, String sql) throws SQLException {

        var statement = connection.createStatement();

        return statement.executeQuery(sql);

    }

    private Statement execute(Connection connection, String sql) throws SQLException {

        var statement = connection.createStatement();

        statement.execute(sql);

        return statement;

    }

}
