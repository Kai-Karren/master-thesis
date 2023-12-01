package de.kaikarren.feedback.data;

import de.kaikarren.complaints.data.Complaint;
import de.kaikarren.complaints.data.DBConnector;
import de.kaikarren.complaints.data.Location;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DBConnectorTest {

    @Test
    void createComplaintInsertQuery() {

        var dbConnector = new DBConnector();

        var complaint = new Complaint.ComplaintBuilder(UUID.randomUUID().toString())
                .setName("Müllbeschwerde in der Adenauerstraße")
                .setStatus("open")
                .setCategory(Complaint.Category.POLLUTION)
                .setLocation(new Location("Adenauerstraße", "42", "Mandelbachtal", "DE", "66399", null, ""))
                .setCreationDate(Instant.now())
                .setLastUpdated(Instant.now())
                .build();

        var query = dbConnector.createComplaintInsertQuery(complaint);

        assertNotNull(query);

    }
}