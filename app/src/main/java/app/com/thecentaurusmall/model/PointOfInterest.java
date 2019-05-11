package app.com.thecentaurusmall.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import app.com.thecentaurusmall.Utils.Utils;

public class PointOfInterest implements SortedListAdapter.ViewModel, Parcelable {


    private final int rank;
    private String id;
    private String name;
    private String category;
    private LatLng _geoloc;
    private long floor_num;
    private String directory_tag;
    private String description;
    private HashMap<String, String> url;


    public PointOfInterest(int rank, String id, String name, String category,
                           LatLng _geoloc, long floor_num, String directory_tag, String description,
                           HashMap<String,String> url) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.category = category;
        this._geoloc = _geoloc;
        this.floor_num = floor_num;
        this.directory_tag = directory_tag;
        this.description = description;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public int getRank() {
        return rank;
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

    public String floorNumberToName(long floor_num) {
        return Utils.floorNumberToName((int) floor_num);
    }

    public String getDirectory_tag() {
        return directory_tag;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getUrl() {
        return url;
    }

    @Override
    public <T> boolean isSameModelAs(T item) {
        if (item instanceof PointOfInterest) {
            final PointOfInterest other = (PointOfInterest) item;
            return other.id == id;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof PointOfInterest) {
            final PointOfInterest other = (PointOfInterest) item;
            if (rank != other.rank) {
                return false;
            }
            return name != null ? name.equals(other.name) : other.name == null;
        }
        return false;
    }

    protected PointOfInterest(Parcel in) {
        rank = in.readInt();
        id = in.readString();
        name = in.readString();
        category = in.readString();
        _geoloc = (LatLng) in.readValue(LatLng.class.getClassLoader());
        floor_num = in.readLong();
        directory_tag = in.readString();
        description = in.readString();
        url = in.readHashMap(HashMap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(rank);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeValue(_geoloc);
        dest.writeLong(floor_num);
        dest.writeString(directory_tag);
        dest.writeString(description);
        dest.writeMap(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PointOfInterest> CREATOR = new Parcelable.Creator<PointOfInterest>() {
        @Override
        public PointOfInterest createFromParcel(Parcel in) {
            return new PointOfInterest(in);
        }

        @Override
        public PointOfInterest[] newArray(int size) {
            return new PointOfInterest[size];
        }
    };

}