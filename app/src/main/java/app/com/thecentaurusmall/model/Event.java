package app.com.thecentaurusmall.model;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class Event {

    private String name;
    private String host;
    private String category;
    private String description;
    private Timestamp start_date;
    private Timestamp end_date;
    private LatLng _geoloc;
    private HashMap<String, String> url;


    public Event(String name, String host, String category, String description, Timestamp start_date, Timestamp end_date, LatLng _geoloc, HashMap<String, String> url) {
        this.name = name;
        this.host = host;
        this.category = category;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this._geoloc = _geoloc;
        this.url = url;
    }

    public Event() {
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public LatLng get_geoloc() {
        return _geoloc;
    }

    public HashMap<String, String> getUrl() {
        return url;
    }
}
