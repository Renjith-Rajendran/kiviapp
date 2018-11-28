package kivi.ugran.com.kds.model.configuration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * JSON Snippet
 * ""blackouts"": [
 * {
 * ""areaName"": ""Via Sotto i Poli Italy"",
 * ""currentLat"": ""45.5627544"",
 * ""currentLng"": ""11.34594949999996"",
 * ""nelat"": ""45.63901245035944"",
 * ""nelng"": ""11.45509211027337"",
 * ""radius"": ""12"",
 * ""swlat"": ""45.48639269882429"",
 * ""swlng"": ""11.23710298038415""
 * }
 */
public class Blackouts implements Parcelable {
    @SerializedName("areaName") private String areaName;

    @SerializedName("currentLat") private String currentLat;

    @SerializedName("currentLng") private String currentLng;

    @SerializedName("nelat") private String nelat;

    @SerializedName("nelng") private String nelng;

    @SerializedName("radius") private String radius;

    @SerializedName("swlat") private String swlat;

    @SerializedName("swlng") private String swlng;

    public Blackouts(String areaName, String currentLat, String currentLng, String nelat, String nelng, String radius,
            String swlat, String swlng) {
        this.areaName = areaName;
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.nelat = nelat;
        this.nelng = nelng;
        this.radius = radius;
        this.swlat = swlat;
        this.swlng = swlng;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public String getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(String currentLng) {
        this.currentLng = currentLng;
    }

    public String getNelat() {
        return nelat;
    }

    public void setNelat(String nelat) {
        this.nelat = nelat;
    }

    public String getNelng() {
        return nelng;
    }

    public void setNelng(String nelng) {
        this.nelng = nelng;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getSwlat() {
        return swlat;
    }

    public void setSwlat(String swlat) {
        this.swlat = swlat;
    }

    public String getSwlng() {
        return swlng;
    }

    public void setSwlng(String swlng) {
        this.swlng = swlng;
    }

    protected Blackouts(Parcel in) {
        areaName = in.readString();
        currentLat = in.readString();
        currentLng = in.readString();
        nelat = in.readString();
        nelng = in.readString();
        radius = in.readString();
        swlat = in.readString();
        swlng = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaName);
        dest.writeString(currentLat);
        dest.writeString(currentLng);
        dest.writeString(nelat);
        dest.writeString(nelng);
        dest.writeString(radius);
        dest.writeString(swlat);
        dest.writeString(swlng);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Blackouts> CREATOR =
            new Parcelable.Creator<Blackouts>() {
                @Override public Blackouts createFromParcel(Parcel in) {
                    return new Blackouts(in);
                }

                @Override public Blackouts[] newArray(int size) {
                    return new Blackouts[size];
                }
            };
}
