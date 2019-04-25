package app.com.thecentaurusmall.model;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.gms.maps.model.LatLng;

import app.com.thecentaurusmall.Utils.Utils;

public class PointOfInterest implements SortedListAdapter.ViewModel {


    private final int rank;
    private String id;
    private String name;
    private String category;
    private LatLng _geoloc;
    private long floor_num;
    private String directory_tag;
    private String description;


    public PointOfInterest(int rank, String id, String name, String category,
                           LatLng _geoloc, long floor_num, String directory_tag, String description) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.category = category;
        this._geoloc = _geoloc;
        this.floor_num = floor_num;
        this.directory_tag = directory_tag;
        this.description = description;
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
}
