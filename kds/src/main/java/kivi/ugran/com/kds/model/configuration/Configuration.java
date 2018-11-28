package kivi.ugran.com.kds.model.configuration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Refer configuration.json JSON Snippet
 */
public class Configuration implements Parcelable {

    @SerializedName("AGE_LIMIT") private int ageLimit;

    @SerializedName("KIVI_LIST_LIMIT") private int kiviListLimit;

    @SerializedName("LOC_UPDATE_INTERVAL") private int locationUpdateInterval;

    @SerializedName("MAX_DISTANCE") private int maxDistance;

    @SerializedName("OTP_EXPIRATION_TIME") private int otpExpirationTime;

    @SerializedName("REST_TIME") private int restTime;

    @SerializedName("blackouts") List<Blackouts> blackouts;

    @SerializedName("kivi_points") Points points;

    @SerializedName("kivi_perfomance_rating") PerfomanceRating perfomanceRating;

    @SerializedName("kivi_availability_rating") AvailabilityRating availabilityRating;

    public List<Blackouts> getBlackouts() {
        return blackouts;
    }

    public void setBlackouts(List<Blackouts> blackouts) {
        this.blackouts = blackouts;
    }

    public Configuration(int ageLimit, int kiviListLimit, int locationUpdateInterval, int maxDistance,
            int otpExpirationTime, int restTime, Points points, PerfomanceRating perfomanceRating,
            AvailabilityRating availabilityRating) {
        this.ageLimit = ageLimit;
        this.kiviListLimit = kiviListLimit;
        this.locationUpdateInterval = locationUpdateInterval;
        this.maxDistance = maxDistance;
        this.otpExpirationTime = otpExpirationTime;
        this.restTime = restTime;
        this.points = points;
        this.perfomanceRating = perfomanceRating;
        this.availabilityRating = availabilityRating;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public int getKiviListLimit() {
        return kiviListLimit;
    }

    public void setKiviListLimit(int kiviListLimit) {
        this.kiviListLimit = kiviListLimit;
    }

    public int getLocationUpdateInterval() {
        return locationUpdateInterval;
    }

    public void setLocationUpdateInterval(int locationUpdateInterval) {
        this.locationUpdateInterval = locationUpdateInterval;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getOtpExpirationTime() {
        return otpExpirationTime;
    }

    public void setOtpExpirationTime(int otpExpirationTime) {
        this.otpExpirationTime = otpExpirationTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }

    public PerfomanceRating getPerfomanceRating() {
        return perfomanceRating;
    }

    public void setPerfomanceRating(PerfomanceRating perfomanceRating) {
        this.perfomanceRating = perfomanceRating;
    }

    public AvailabilityRating getAvailabilityRating() {
        return availabilityRating;
    }

    public void setAvailabilityRating(AvailabilityRating availabilityRating) {
        this.availabilityRating = availabilityRating;
    }

    protected Configuration(Parcel in) {
        ageLimit = in.readInt();
        kiviListLimit = in.readInt();
        locationUpdateInterval = in.readInt();
        maxDistance = in.readInt();
        otpExpirationTime = in.readInt();
        restTime = in.readInt();
        points = (Points) in.readValue(Points.class.getClassLoader());
        perfomanceRating = (PerfomanceRating) in.readValue(PerfomanceRating.class.getClassLoader());
        availabilityRating = (AvailabilityRating) in.readValue(AvailabilityRating.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ageLimit);
        dest.writeInt(kiviListLimit);
        dest.writeInt(locationUpdateInterval);
        dest.writeInt(maxDistance);
        dest.writeInt(otpExpirationTime);
        dest.writeInt(restTime);
        dest.writeValue(points);
        dest.writeValue(perfomanceRating);
        dest.writeValue(availabilityRating);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Configuration> CREATOR =
            new Parcelable.Creator<Configuration>() {
                @Override public Configuration createFromParcel(Parcel in) {
                    return new Configuration(in);
                }

                @Override public Configuration[] newArray(int size) {
                    return new Configuration[size];
                }
            };
}
