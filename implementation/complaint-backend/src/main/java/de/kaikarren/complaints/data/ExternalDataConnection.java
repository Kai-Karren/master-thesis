package de.kaikarren.complaints.data;

import org.json.JSONObject;

import java.util.List;

public interface ExternalDataConnection {

    public List<JSONObject> fetchData(Complaint complaint);

}
