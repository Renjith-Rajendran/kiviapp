package kivi.ugran.com.kds.model.search;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import kivi.ugran.com.kds.model.registration.Coordinates;

public class Place implements Parcelable {
    @SerializedName("coordinates") private Coordinates coordinates;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @SerializedName("kivi_content_rating_count") private int contentRatingCount;

    @SerializedName("kivi_available_rating_count") private int availableRatingCount;

    @SerializedName("kivi_system_version") private String systemVersion;

    @SerializedName("kivi_rest_time") private int restTime;

    @SerializedName("isUpdated") private Boolean isUpdated;

    @SerializedName("network_type") private String networkType;

    @SerializedName("device_id") private String deviceId;

    @SerializedName("kivi_available_rating") private int availableRating;

    @SerializedName("kivi_id") private String kiviId;

    @SerializedName("notification_vibration") private Boolean notificationVibration;

    @SerializedName("kivi_camera_resoluition") private String cameraResolution;

    @SerializedName("kivi_name") private String kiviName;

    @SerializedName("kivi_requestor_rating_count") private int requestorRatingCount;

    @SerializedName("email") private String email;

    @SerializedName("kivi_version") private String kiviVersion;

    @SerializedName("kivi_content_rating") private int contentRating;

    @SerializedName("kivi_device_token") private String deviceToken;

    @SerializedName("broadcasting_type") private String broadcastingType;

    @SerializedName("kivi_requestor_rating") private int requestorRating;

    @SerializedName("connectivity_type") private String connectivityType;

    @SerializedName("notification_sound") private Boolean notificationSound;

    @SerializedName("kivi_wallet") private int kivWallet;

    @SerializedName("registrationStatus") private Boolean registrationStatus;

    @SerializedName("distanceFromKivi") private float distanceFromKivi;

    @SerializedName("createdAt") private String createdAt;

    @SerializedName("updatedAt") private String updatedAt;

    public int getContentRatingCount() {
        return contentRatingCount;
    }

    public int getAvailableRatingCount() {
        return availableRatingCount;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getAvailableRating() {
        return availableRating;
    }

    public String getKiviName() {
        return kiviName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public int getKivWallet() {
        return kivWallet;
    }

    public Place(Place place) {
        coordinates = (Coordinates) place.getCoordinates();
        contentRatingCount = place.contentRatingCount;
        availableRatingCount = place.availableRatingCount;
        systemVersion = place.systemVersion;
        restTime = place.restTime;
        isUpdated = place.isUpdated;
        networkType = place.networkType;
        deviceId = place.deviceId;
        availableRating = place.availableRating;
        kiviId = place.kiviId;
        notificationVibration = place.notificationVibration;
        cameraResolution = place.cameraResolution;
        kiviName = place.kiviName;
        requestorRatingCount = place.requestorRatingCount;
        email = place.email;
        kiviVersion = place.kiviVersion;
        contentRating = place.contentRating;
        deviceToken = place.deviceToken;
        broadcastingType = place.broadcastingType;
        requestorRating = place.requestorRating;
        connectivityType = place.connectivityType;
        notificationSound =place.notificationSound;
        kivWallet = place.kivWallet;
        registrationStatus = place.registrationStatus;
        distanceFromKivi =place.distanceFromKivi;
        createdAt = place.createdAt;
        updatedAt = place.updatedAt;
    }

    protected Place(Parcel in) {
        coordinates = (Coordinates) in.readValue(Coordinates.class.getClassLoader());
        contentRatingCount = in.readInt();
        availableRatingCount = in.readInt();
        systemVersion = in.readString();
        restTime = in.readInt();
        byte isUpdatedVal = in.readByte();
        isUpdated = isUpdatedVal == 0x02 ? null : isUpdatedVal != 0x00;
        networkType = in.readString();
        deviceId = in.readString();
        availableRating = in.readInt();
        kiviId = in.readString();
        byte notificationVibrationVal = in.readByte();
        notificationVibration = notificationVibrationVal == 0x02 ? null : notificationVibrationVal != 0x00;
        cameraResolution = in.readString();
        kiviName = in.readString();
        requestorRatingCount = in.readInt();
        email = in.readString();
        kiviVersion = in.readString();
        contentRating = in.readInt();
        deviceToken = in.readString();
        broadcastingType = in.readString();
        requestorRating = in.readInt();
        connectivityType = in.readString();
        byte notificationSoundVal = in.readByte();
        notificationSound = notificationSoundVal == 0x02 ? null : notificationSoundVal != 0x00;
        kivWallet = in.readInt();
        byte registrationStatusVal = in.readByte();
        registrationStatus = registrationStatusVal == 0x02 ? null : registrationStatusVal != 0x00;
        distanceFromKivi = in.readFloat();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(coordinates);
        dest.writeInt(contentRatingCount);
        dest.writeInt(availableRatingCount);
        dest.writeString(systemVersion);
        dest.writeInt(restTime);
        if (isUpdated == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isUpdated ? 0x01 : 0x00));
        }
        dest.writeString(networkType);
        dest.writeString(deviceId);
        dest.writeInt(availableRating);
        dest.writeString(kiviId);
        if (notificationVibration == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (notificationVibration ? 0x01 : 0x00));
        }
        dest.writeString(cameraResolution);
        dest.writeString(kiviName);
        dest.writeInt(requestorRatingCount);
        dest.writeString(email);
        dest.writeString(kiviVersion);
        dest.writeInt(contentRating);
        dest.writeString(deviceToken);
        dest.writeString(broadcastingType);
        dest.writeInt(requestorRating);
        dest.writeString(connectivityType);
        if (notificationSound == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (notificationSound ? 0x01 : 0x00));
        }
        dest.writeInt(kivWallet);
        if (registrationStatus == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (registrationStatus ? 0x01 : 0x00));
        }
        dest.writeFloat(distanceFromKivi);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Place> CREATOR =
            new Parcelable.Creator<Place>() {
                @Override public Place createFromParcel(Parcel in) {
                    return new Place(in);
                }

                @Override public Place[] newArray(int size) {
                    return new Place[size];
                }
            };
}
//public class Place {
//    @SerializedName("coordinates")
//    private Coordinates coordinates;
//
//    @SerializedName("kivi_content_rating_count")
//    private int contentRatingCount;
//
//    @SerializedName("kivi_available_rating_count")
//    private int availableRatingCount;
//
//    @SerializedName("kivi_system_version")
//    private String systemVersion;
//
//    @SerializedName("kivi_rest_time")
//    private int restTime;
//
//    @SerializedName("isUpdated")
//    private Boolean isUpdated;
//
//    @SerializedName("network_type")
//    private String networkType;
//
//    @SerializedName("device_id")
//    private String deviceId;
//
//    @SerializedName("kivi_available_rating")
//    private int availableRating;
//
//    @SerializedName("kivi_id")
//    private String kiviId;
//
//    @SerializedName("notification_vibration")
//    private Boolean notificationVibration;
//
//    @SerializedName("kivi_camera_resoluition")
//    private String cameraResolution;
//
//    @SerializedName("kivi_name")
//    private String kiviName;
//
//    @SerializedName("kivi_requestor_rating_count")
//    private int requestorRatingCount;
//
//    @SerializedName("email")
//    private String email;
//
//    @SerializedName("kivi_version")
//    private String kiviVersion;
//
//    @SerializedName("kivi_content_rating")
//    private int contentRating;
//
//    @SerializedName("kivi_device_token")
//    private String deviceToken;
//
//    @SerializedName("broadcasting_type")
//    private String broadcastingType;
//
//    @SerializedName("kivi_requestor_rating")
//    private int requestorRating;
//
//    @SerializedName("connectivity_type")
//    private String connectivityType;
//
//    @SerializedName("notification_sound")
//    private Boolean notificationSound;
//
//    @SerializedName("kivi_wallet")
//    private int kivWallet;
//
//    @SerializedName("registrationStatus")
//    private Boolean registrationStatus;
//
//    @SerializedName("distanceFromKivi")
//    private float distanceFromKivi;
//
//    @SerializedName("createdAt")
//    private String createdAt;
//
//    @SerializedName("updatedAt")
//    private String updatedAt;
//}

//@SerializedName("user_roles")
//private Roles roles;

//@SerializedName("kivi_type")
//private Type type;

//@SerializedName("kivi_language")
//private Type language;
/*
{
    "places": [
        {
            "_id": "5ba450c618b2510e5989061b",
            "kivi_content_rating_count": 1,
            "kivi_system_version": "10.2",
            "user_roles": {
                "admin": false,
                "user": true
            },
            "kivi_rest_time": 10,
            "isUpdated": false,
            "network_type": "Wi-Fi",
            "device_id": "F8E70D67-2CAC-4AC0-8493-FA6565AA4350",
            "kivi_type": {
                "photo": true,
                "video": true
            },
            "blackouts": [],
            "kivi_available_rating": 0,
            "kivi_id": "divyaEDE948B9-0A5D-4186-9EE5-79430F1730B8",
            "notification_vibration": false,
            "kivi_status": "Available",
            "kivi_language": [
                "English"
            ],
            "kivi_camera_resoluition": "320*480",
            "kivi_name": "divya",
            "kivi_requestor_rating_count": 2,
            "email": "divyajose@gmail.com",
            "kivi_version": "Version 2.3 (6)",
            "kivi_content_rating": 0,
            "kivi_device_token": "f4e5b57ab717854072535c379ca9208c159704275f496e53460fd6183d6499a5",
            "broadcasting_type": "Active",
            "kivi_requestor_rating": 9,
            "connectivity_type": "GPS",
            "coordinates": {
                "lng": -117.7355,
                "lat": 33.58358
            },
            "kivi_available_rating_count": 0,
            "notification_sound": true,
            "kivi_wallet": 102,
            "createdAt": "2018-09-21T02:00:38.660Z",
            "updatedAt": "2018-10-23T05:02:46.300Z",
            "registrationStatus": true,
            "distanceFromKivi": 0.022564398491309774
        }
    ],
    "request": "success"
}
 */