package kivi.ugran.com.kds.model.configuration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * JSON Snippet
 * ""kivi_availability_rating"": {
 * ""ACCEPT"": 1,
 * ""NO_RESPONSE"": -1,
 * ""REJECT"": 0
 * }
 */
public class AvailabilityRating implements Parcelable {
    @SerializedName("ACCEPT") private int accept;

    @SerializedName("NO_RESPONSE") private int noResponse;

    @SerializedName("REJECT") private int reject;

    public AvailabilityRating(int accept, int noResponse, int reject) {
        this.accept = accept;
        this.noResponse = noResponse;
        this.reject = reject;
    }

    public int getAccept() {
        return accept;
    }

    public void setAccept(int accept) {
        this.accept = accept;
    }

    public int getNoResponse() {
        return noResponse;
    }

    public void setNoResponse(int noResponse) {
        this.noResponse = noResponse;
    }

    public int getReject() {
        return reject;
    }

    public void setReject(int reject) {
        this.reject = reject;
    }

    protected AvailabilityRating(Parcel in) {
        accept = in.readInt();
        noResponse = in.readInt();
        reject = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(accept);
        dest.writeInt(noResponse);
        dest.writeInt(reject);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<AvailabilityRating> CREATOR =
            new Parcelable.Creator<AvailabilityRating>() {
                @Override public AvailabilityRating createFromParcel(Parcel in) {
                    return new AvailabilityRating(in);
                }

                @Override public AvailabilityRating[] newArray(int size) {
                    return new AvailabilityRating[size];
                }
            };
}