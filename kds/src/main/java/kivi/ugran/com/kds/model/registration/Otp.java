package kivi.ugran.com.kds.model.registration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/*
"{
  ""device_id"": "testy2",
  ""email"": ""polusipad@gmail.com"",
  ""existingKivi"": true,
  ""kivi_name"": ""Polus Ipad"",
  ""otp"": 407186
}"

And

{
    "otp": "266709",
    "kivi_name": "dufugi",
    "email": "jcjc@dg.com",
    "dob": "12/12/1234",
    "device_id": "testy2",
    "request": "success"
}
is response
 */
public class Otp implements Parcelable {
    @SerializedName("device_id") private String deviceId;

    @SerializedName("email") private String email;

    @SerializedName("kivi_name") private String kiviName;

    /**
     * This one is coming in response when Registration is submitted for again for the same device id
     */
    @SerializedName("existingKivi") private Boolean existingKivi;

    @SerializedName("otp") private String otp;

    @SerializedName("dob") private String dob;

    public Otp(String deviceId, String email, String kiviName, Boolean existingKivi, String otp, String dob) {
        this.deviceId = deviceId;
        this.email = email;
        this.kiviName = kiviName;
        this.existingKivi = existingKivi;
        this.otp = otp;
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKiviName() {
        return kiviName;
    }

    public void setKiviName(String kiviName) {
        this.kiviName = kiviName;
    }

    public Boolean getExistingKivi() {
        return existingKivi;
    }

    public void setExistingKivi(Boolean existingKivi) {
        this.existingKivi = existingKivi;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    protected Otp(Parcel in) {
        deviceId = in.readString();
        email = in.readString();
        kiviName = in.readString();
        byte existingKiviVal = in.readByte();
        existingKivi = existingKiviVal == 0x02 ? null : existingKiviVal != 0x00;
        otp = in.readString();
        dob = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceId);
        dest.writeString(email);
        dest.writeString(kiviName);
        if (existingKivi == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (existingKivi ? 0x01 : 0x00));
        }
        dest.writeString(otp);
        dest.writeString(dob);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Otp> CREATOR = new Parcelable.Creator<Otp>() {
        @Override public Otp createFromParcel(Parcel in) {
            return new Otp(in);
        }

        @Override public Otp[] newArray(int size) {
            return new Otp[size];
        }
    };
}
