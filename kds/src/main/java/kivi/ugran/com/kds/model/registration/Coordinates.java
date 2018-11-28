package kivi.ugran.com.kds.model.registration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class Coordinates implements Parcelable {
    @SuppressWarnings("unused") public static final Parcelable.Creator<Coordinates> CREATOR =
            new Parcelable.Creator<Coordinates>() {
                @Override public Coordinates createFromParcel(Parcel in) {
                    return new Coordinates(in);
                }

                @Override public Coordinates[] newArray(int size) {
                    return new Coordinates[size];
                }
            };

    @SerializedName("lng") private double lng;
    @SerializedName("lat") private double lat;

    public Coordinates(double lat, double lng) {
        this.lng = lng;
        this.lat = lat;
    }

    protected Coordinates(Parcel in) {
        lng = in.readDouble();
        lat = in.readDouble();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lng);
        dest.writeDouble(lat);
    }
}
