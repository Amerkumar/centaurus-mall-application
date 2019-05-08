package app.com.thecentaurusmall.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;

import java.util.HashMap;

import app.com.thecentaurusmall.Utils.Utils;

public class Offer  {

    private final int rank;
    private String id;
    private String category;
    private LatLng _geoloc;
    private Timestamp end_date;
    private long floor_num;
    private String name;
    private long percentage;
    private Timestamp start_date;
    private HashMap<String, String> url;
    private String description;


    public Offer(int rank, String id, String category, LatLng _geoloc, Timestamp end_date, long floor_num,
                 String name, long percentage, Timestamp start_date, HashMap<String, String> url, String description) {
        this.rank = rank;
        this.id = id;
        this.category = category;
        this._geoloc = _geoloc;
        this.end_date = end_date;
        this.floor_num = floor_num;
        this.name = name;
        this.percentage = percentage;
        this.start_date = start_date;
        this.url = url;
        this.description = description;
    }

    public int getRank() {
        return rank;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public LatLng get_geoloc() {
        return _geoloc;
    }

    public Timestamp getEnd_date() {
        return end_date;
    }

    public long getFloor_num() {
        return floor_num;
    }

    public String getName() {
        return name;
    }

    public long getPercentage() {
        return percentage;
    }

    public Timestamp getStart_date() {
        return start_date;
    }

    public HashMap<String, String> getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}