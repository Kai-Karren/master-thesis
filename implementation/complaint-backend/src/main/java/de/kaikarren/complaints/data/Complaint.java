package de.kaikarren.complaints.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Complaint {

    private String uuid;
    private String name;
    private String summary;
    private String status;
    private Category category;
    private Location location;
    private String reason;
    private String severity;

    private List<User> users;
    private List<Conversation> conversations;

    @JsonProperty("creation_date")
    private Instant creationDate;
    @JsonProperty("last_updated")
    private Instant lastUpdated;

    public Complaint(ComplaintBuilder complaintBuilder){
        this.uuid = complaintBuilder.getUuid();
        this.name = complaintBuilder.getName();
        this.summary = complaintBuilder.getSummary();
        this.status = complaintBuilder.getStatus();
        this.category = complaintBuilder.getCategory();
        this.location = complaintBuilder.getLocation();
        this.reason = complaintBuilder.getReason();
        this.severity = complaintBuilder.getSeverity();
        this.users = complaintBuilder.getUsers();
        this.conversations = complaintBuilder.getConversations();
        this.creationDate = complaintBuilder.getCreationDate();
        this.lastUpdated = complaintBuilder.getLastUpdated();
    }

    public enum Category {
        @JsonProperty("noise")
        NOISE,
        @JsonProperty("pollution")
        POLLUTION,
        @JsonProperty("transportation")
        TRANSPORTATION,
        @JsonProperty("infrastructure")
        INFRASTRUCTURE,
        @JsonProperty("other")
        OTHER
    }

    @Getter
    public static class ComplaintBuilder {

        private String uuid;
        private String name;
        private String summary;
        private String status;
        private Category category;
        private String text;
        private Location location;
        private String reason;
        private String severity;

        private List<User> users = new ArrayList<>();
        private List<Conversation> conversations= new ArrayList<>();

        private Instant creationDate;
        private Instant lastUpdated;

        public ComplaintBuilder(String uuid) {
            this.uuid = uuid;
        }

        public ComplaintBuilder setName(String name){
            this.name = name;
            return this;
        }

        public ComplaintBuilder setSummary(String summary){
            this.summary = summary;
            return this;
        }
        public ComplaintBuilder setStatus(String status){
            this.status = status;
            return this;
        }

        public ComplaintBuilder setCategory(Category category){
            this.category = category;
            return this;
        }

        public ComplaintBuilder setLocation(Location location){
            this.location = location;
            return this;
        }

        public ComplaintBuilder setReason(String reason){
            this.reason = reason;
            return this;
        }

        public ComplaintBuilder setSeverity(String severity){
            this.severity = severity;
            return this;
        }

        public ComplaintBuilder setUsers(List<User> users){
            this.users = users;
            return this;
        }

        public ComplaintBuilder addUser(User user){
            this.users.add(user);
            return this;
        }

        public ComplaintBuilder setConversations(List<Conversation> conversations){
            this.conversations = conversations;
            return this;
        }

        public ComplaintBuilder addConversation(Conversation conversation){
            this.conversations.add(conversation);
            return this;
        }

        public ComplaintBuilder setCreationDate(Instant creationDate){
            this.creationDate = creationDate;
            return this;
        }

        public ComplaintBuilder setLastUpdated(Instant lastUpdated){
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Complaint build(){
            return new Complaint(this);
        }

    }

}
