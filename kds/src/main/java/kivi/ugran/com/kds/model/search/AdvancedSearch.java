package kivi.ugran.com.kds.model.search;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import kivi.ugran.com.kds.model.registration.Coordinates;

/*
{
  "broadcasting_type": "",
  "kivi_rating_average": "",
  "kivi_type": "",
  "language": "",
   "search_radius": "5 KiloMeter"
  "location": {
    "lat": "8.560573",
    "lng": "76.8807"
  },

}
 */

//Generated using http://www.parcelabler.com/
public class AdvancedSearch implements Parcelable {
    @SerializedName("location") private Coordinates coordinates;

    @SerializedName("search_radius") private String searchRadius;

    @SerializedName("broadcasting_type") private String broadcastingType;

    @SerializedName("kivi_rating_average") private String kiviRatingAverage;

    @SerializedName("kivi_type") private String kiviType;

    @SerializedName("language") private String language;

    public AdvancedSearch(Coordinates coordinates, String searchRadius, String broadcastingType,
            String kiviRatingAverage, String kiviType, String language) {
        this.coordinates = coordinates;
        this.searchRadius = searchRadius;
        this.broadcastingType = broadcastingType;
        this.kiviRatingAverage = kiviRatingAverage;
        this.kiviType = kiviType;
        this.language = language;
    }

    protected AdvancedSearch(Parcel in) {
        coordinates = (Coordinates) in.readValue(Coordinates.class.getClassLoader());
        broadcastingType = in.readString();
        kiviRatingAverage = in.readString();
        kiviType = in.readString();
        language = in.readString();
        searchRadius = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(coordinates);
        dest.writeString(broadcastingType);
        dest.writeString(kiviRatingAverage);
        dest.writeString(kiviType);
        dest.writeString(language);
        dest.writeString(searchRadius);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<AdvancedSearch> CREATOR =
            new Parcelable.Creator<AdvancedSearch>() {
                @Override public AdvancedSearch createFromParcel(Parcel in) {
                    return new AdvancedSearch(in);
                }

                @Override public AdvancedSearch[] newArray(int size) {
                    return new AdvancedSearch[size];
                }
            };
}
