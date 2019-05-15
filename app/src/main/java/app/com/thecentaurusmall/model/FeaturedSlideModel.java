package app.com.thecentaurusmall.model;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Objects;

public class FeaturedSlideModel {


    private final String id;
    private String name;
    private HashMap<String,String> url;

    public FeaturedSlideModel(String id, String name, HashMap<String, String> url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getUrl() {
        return url;
    }

    public void setUrl(HashMap<String, String> url) {
        this.url = url;
    }
}
