package app.com.thecentaurusmall.model;

import androidx.annotation.NonNull;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.Objects;

public class Category implements SortedListAdapter.ViewModel {


    private final int rank;
    private String name;
    private int count;


    public Category(int rank, String name, int count) {
        this.rank = rank;
        this.name = name;
        this.count = count;
    }



    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }


    public int getCount() {
        return count;
    }

    public int incrementCount() {
        return this.count++;
    }

    @Override
    public <T> boolean isSameModelAs(T item) {
        if (item instanceof Category) {
            final Category other = (Category) item;
            return other.rank == rank;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
