package kivi.ugran.com.kds.model.configuration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * JSON Snippet
 * ""kivi_perfomance_rating"": {
 * ""FIVE_STARS"": 3,
 * ""FOUR_STARS"": 2,
 * ""ONE_STARS"": -1,
 * ""THREE_STARS"": 1,
 * ""TWO_STARS"": 0,
 * ""ZERO_STARS"": -2
 * },
 */
public class PerfomanceRating implements Parcelable {
    @SerializedName("FIVE_STARS") private int fiveStar;

    @SerializedName("FOUR_STARS") private int fourStar;

    @SerializedName("THREE_STARS") private int threeStart;

    @SerializedName("TWO_STARS") private int twoStar;

    @SerializedName("ONE_STARS") private int oneStar;

    @SerializedName("ZERO_STARS") private int zeroStar;

    public PerfomanceRating(int fiveStar, int fourStar, int threeStart, int twoStar, int oneStar, int zeroStar) {
        this.fiveStar = fiveStar;
        this.fourStar = fourStar;
        this.threeStart = threeStart;
        this.twoStar = twoStar;
        this.oneStar = oneStar;
        this.zeroStar = zeroStar;
    }

    public int getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(int fiveStar) {
        this.fiveStar = fiveStar;
    }

    public int getFourStar() {
        return fourStar;
    }

    public void setFourStar(int fourStar) {
        this.fourStar = fourStar;
    }

    public int getThreeStart() {
        return threeStart;
    }

    public void setThreeStart(int threeStart) {
        this.threeStart = threeStart;
    }

    public int getTwoStar() {
        return twoStar;
    }

    public void setTwoStar(int twoStar) {
        this.twoStar = twoStar;
    }

    public int getOneStar() {
        return oneStar;
    }

    public void setOneStar(int oneStar) {
        this.oneStar = oneStar;
    }

    public int getZeroStar() {
        return zeroStar;
    }

    public void setZeroStar(int zeroStar) {
        this.zeroStar = zeroStar;
    }

    protected PerfomanceRating(Parcel in) {
        fiveStar = in.readInt();
        fourStar = in.readInt();
        threeStart = in.readInt();
        twoStar = in.readInt();
        oneStar = in.readInt();
        zeroStar = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fiveStar);
        dest.writeInt(fourStar);
        dest.writeInt(threeStart);
        dest.writeInt(twoStar);
        dest.writeInt(oneStar);
        dest.writeInt(zeroStar);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<PerfomanceRating> CREATOR =
            new Parcelable.Creator<PerfomanceRating>() {
                @Override public PerfomanceRating createFromParcel(Parcel in) {
                    return new PerfomanceRating(in);
                }

                @Override public PerfomanceRating[] newArray(int size) {
                    return new PerfomanceRating[size];
                }
            };
}
