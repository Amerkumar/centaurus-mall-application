package app.com.thecentaurusmall.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LatLng implements Parcelable {
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

    protected LatLng(Parcel in) {
        lat = in.readByte() == 0x00 ? null : in.readDouble();
        lng = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (lat == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(lng);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LatLng> CREATOR = new Parcelable.Creator<LatLng>() {
        @Override
        public LatLng createFromParcel(Parcel in) {
            return new LatLng(in);
        }

        @Override
        public LatLng[] newArray(int size) {
            return new LatLng[size];
        }
    };
}