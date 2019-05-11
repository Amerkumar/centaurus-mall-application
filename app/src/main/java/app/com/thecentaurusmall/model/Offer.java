package app.com.thecentaurusmall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.HashMap;

public class Offer implements Parcelable {

    private int rank;
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


    public Offer() {

    }

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

    protected Offer(Parcel in) {
        rank = in.readInt();
        id = in.readString();
        category = in.readString();
        _geoloc = (LatLng) in.readValue(LatLng.class.getClassLoader());
        end_date = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        floor_num = in.readLong();
        name = in.readString();
        percentage = in.readLong();
        start_date = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        url = (HashMap) in.readValue(HashMap.class.getClassLoader());
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rank);
        dest.writeString(id);
        dest.writeString(category);
        dest.writeValue(_geoloc);
        dest.writeValue(end_date);
        dest.writeLong(floor_num);
        dest.writeString(name);
        dest.writeLong(percentage);
        dest.writeValue(start_date);
        dest.writeValue(url);
        dest.writeString(description);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };
}