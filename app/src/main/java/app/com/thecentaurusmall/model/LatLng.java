package app.com.thecentaurusmall.model;

public class LatLng {
    private Double lat;
    private Double lng;

    public LatLng() {}

    public LatLng(Double latitude, Double longitude) {
        this.lat = latitude;
        this.lng = longitude;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
