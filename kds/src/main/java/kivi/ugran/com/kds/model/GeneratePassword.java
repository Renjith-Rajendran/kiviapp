package kivi.ugran.com.kds.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/*
{
  "device_id": "07E30613-3DF2-40D3-8665-70115F53BE8B",
  "isSubmitted": 1,
   "kivi_status":"Available"
}
//  "kivi_status":"Available" is not mandatory
 */
public class GeneratePassword implements Parcelable {
    @SerializedName("device_id") private String device_id;

    @SerializedName("isSubmitted") private int isSubmitted;

    /**
     * This one is from response when passwordgeneration is submitted
     */
    @SerializedName("registrationCompleted") private Boolean registrationCompleted;

    protected GeneratePassword(Parcel in) {
        device_id = in.readString();
        isSubmitted = in.readInt();
        byte registrationCompletedVal = in.readByte();
        registrationCompleted = registrationCompletedVal == 0x02 ? null : registrationCompletedVal != 0x00;
    }

    public GeneratePassword(String device_id, int isSubmitted) {
        this.device_id = device_id;
        this.isSubmitted = isSubmitted;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(device_id);
        dest.writeInt(isSubmitted);
        if (registrationCompleted == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (registrationCompleted ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<GeneratePassword> CREATOR =
            new Parcelable.Creator<GeneratePassword>() {
                @Override public GeneratePassword createFromParcel(Parcel in) {
                    return new GeneratePassword(in);
                }

                @Override public GeneratePassword[] newArray(int size) {
                    return new GeneratePassword[size];
                }
            };

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public int getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(int isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public Boolean getRegistrationCompleted() {
        return registrationCompleted;
    }

    public void setRegistrationCompleted(Boolean registrationCompleted) {
        this.registrationCompleted = registrationCompleted;
    }
}
