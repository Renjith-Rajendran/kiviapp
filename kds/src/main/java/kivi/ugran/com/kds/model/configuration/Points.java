package kivi.ugran.com.kds.model.configuration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ""kivi_points"": {
 * ""deduction_points"": 2,
 * ""gain_points"": 2,
 * ""total_points"": 100
 * }
 */
public class Points implements Parcelable {
    @SerializedName("deduction_points") private String deductionPoints;

    @SerializedName("gain_points") private String gainPoints;

    @SerializedName("total_points") private String totalPoints;

    public Points(String deductionPoints, String gainPoints, String totalPoints) {
        this.deductionPoints = deductionPoints;
        this.gainPoints = gainPoints;
        this.totalPoints = totalPoints;
    }

    public String getDeductionPoints() {
        return deductionPoints;
    }

    public void setDeductionPoints(String deductionPoints) {
        this.deductionPoints = deductionPoints;
    }

    public String getGainPoints() {
        return gainPoints;
    }

    public void setGainPoints(String gainPoints) {
        this.gainPoints = gainPoints;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    protected Points(Parcel in) {
        deductionPoints = in.readString();
        gainPoints = in.readString();
        totalPoints = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deductionPoints);
        dest.writeString(gainPoints);
        dest.writeString(totalPoints);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Points> CREATOR =
            new Parcelable.Creator<Points>() {
                @Override public Points createFromParcel(Parcel in) {
                    return new Points(in);
                }

                @Override public Points[] newArray(int size) {
                    return new Points[size];
                }
            };
}

/*

 */