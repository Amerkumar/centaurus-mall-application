package app.com.thecentaurusmall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class Event implements Parcelable {

    private String name;
    private String host;
    private String category;
    private String description;
    private Timestamp start_date;
    private Timestamp end_date;
    private LatLng _geoloc;
    private long floor;
    private HashMap<String, String> url;


    public Event(String name, String host, String category, String description, Timestamp start_date, Timestamp end_date, LatLng _geoloc, long floor, HashMap<String, String> url) {
        this.name = name;
        this.host = host;
        this.category = category;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this._geoloc = _geoloc;
        this.floor = floor;
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

    protected Event(Parcel in) {
        name = in.readString();
        host = in.readString();
        category = in.readString();
        description = in.readString();
        start_date = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        end_date = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        _geoloc = (LatLng) in.readValue(LatLng.class.getClassLoader());
        floor = in.readLong();
        url = (HashMap) in.readValue(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(host);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeValue(start_date);
        dest.writeValue(end_date);
        dest.writeValue(_geoloc);
        dest.writeLong(floor);
        dest.writeValue(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public long getFloor() {
        return floor;
    }

    public void setFloor(long floor) {
        this.floor = floor;
    }
}