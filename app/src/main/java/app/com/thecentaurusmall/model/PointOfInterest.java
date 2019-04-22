package app.com.thecentaurusmall.model;

import com.google.android.gms.maps.model.LatLng;

public class PointOfInterest {

    private String id;
    private String name;
    private String category;
    private LatLng _geoloc;
    private long floor_num;


    public PointOfInterest(String id, String name, String category, LatLng _geoloc, long floor_num) {
        this.id = id;
        this.name = name;
        this.category = category;
        this._geoloc = _geoloc;
        this.floor_num = floor_num;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public LatLng get_geoloc() {
        return _geoloc;
    }

    public long getFloor_num() {
        return floor_num;
    }
}
