package app.com.thecentaurusmall.model;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import app.com.thecentaurusmall.Utils.Utils;

public class Category implements SortedListAdapter.ViewModel {


    private final int rank;
    private String id;
    private String name;
    private String color;
    private String count;


    public Category(int rank, String id, String name, String color, String count) {
        this.rank = rank;
        this.id = id;
        this.name = name;
        this.color = color;
        this.count = count;
    }



    public int getRank() {
        return rank;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getCount() {
        return count;
    }

    @Override
    public <T> boolean isSameModelAs(T item) {
        if (item instanceof Category) {
            final Category other = (Category) item;
            return other.id == id;
        }
        return false;
    }

    @Override
    public <T> boolean isContentTheSameAs(@NonNull T item) {
        if (item instanceof Category) {
            final Category other = (Category) item;
            if (rank != other.rank) {
                return false;
            }
            return Objects.equals(name, other.name);
        }
        return false;
    }
}
