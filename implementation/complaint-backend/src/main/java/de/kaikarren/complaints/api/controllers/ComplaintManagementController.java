package de.kaikarren.complaints.api.controllers;

import de.kaikarren.complaints.data.Complaint;
import de.kaikarren.complaints.data.DBConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ComplaintManagementController {

    @Autowired
    private DBConnector connector;

    @PostMapping("/complaints")
    public String addComplaint(@RequestBody Complaint complaint) throws SQLException {

        var assignedUuid = connector.addComplaintToDatabase(complaint);

        return assignedUuid;

    }

    @GetMapping("/complaints/{uuid}")
    public Complaint getComplaint(@PathVariable String uuid) throws SQLException {

        return connector.getComplaintWithUuid(uuid);

    }

    @PostMapping("/complaints/{uuid}/update")
    public Complaint updateComplaint(@PathVariable String uuid, @RequestBody Complaint complaint) throws SQLException {

        complaint.setUuid(uuid);

        return connector.updateComplaintInDatabase(complaint);

    }

    @GetMapping("/complaints")
    public List<Complaint> getComplaints() throws SQLException {

        return connector.getComplaints();

    }

    @GetMapping("/complaints/search")
    public List<Complaint> getComplaintsSearch(
            @RequestParam(required = false) String timestamp,
            @RequestParam(required = false) String category
    ) throws SQLException {

        if (category != null){
            category = category.toUpperCase();
        }

        if (category != null && timestamp == null){
            return connector.getComplaintsFilteredByTopicCategory(category);
        }

        if (timestamp != null && category == null){
            return connector.getComplaintsFilteredByTime(timestamp);
        }

        if(timestamp != null){
            return connector.getComplaintsFilteredByTimestampAndTopicCategory(timestamp, category);
        }

        return new ArrayList<>();

    }



}
