package kivi.ugran.com.kds.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import kivi.ugran.com.kds.model.registration.Coordinates;

/*
[
   {
     "coordinates":{
        "lng":-117.72503662109375,
        "lat":33.58558654785156

    },
    "kivi_status":"Offline",
   "deviceId":"testy2",
   "kivi_device_token": "eAZJ3yctBUI:APA91bGvtXIywJDNi_EDTIU92Sa6_Kpk6peiZKOqQbyW4NL2MCzvCRCeCv7tIo605UnwjGIyEfpMAk-LkP2ei-zumRWMZ_r9BduF0dHfHjtKCmm_RJtQ4tFCjOo_kalyqjWr1kUb-0di"
  }
]

And response is

{
    "title": "Success",
    "request": "success"
}
 */
public class UpdateKiviData implements Parcelable {
    @SerializedName("coordinates") Coordinates coordinates;

    @SerializedName("deviceId") private String deviceId;

    @SerializedName("kivi_device_token") private String deviceToken;

    @SerializedName("kivi_status") private String status;

    public UpdateKiviData(Coordinates coordinates, String deviceId, String deviceToken, String status) {
        this.coordinates = coordinates;
        this.deviceId = deviceId;
        this.deviceToken = deviceToken;
        this.status = status;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected UpdateKiviData(Parcel in) {
        coordinates = (Coordinates) in.readValue(Coordinates.class.getClassLoader());
        deviceId = in.readString();
        deviceToken = in.readString();
        status = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(coordinates);
        dest.writeString(deviceId);
        dest.writeString(deviceToken);
        dest.writeString(status);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<UpdateKiviData> CREATOR =
            new Parcelable.Creator<UpdateKiviData>() {
                @Override public UpdateKiviData createFromParcel(Parcel in) {
                    return new UpdateKiviData(in);
                }

                @Override public UpdateKiviData[] newArray(int size) {
                    return new UpdateKiviData[size];
                }
            };
}