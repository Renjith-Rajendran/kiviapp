package kivi.ugran.com.kds.model.search;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/*
{
  "device_id":"testy2",
  "lat": "33.583377299999995",
  "limit": 30,
  "lng": "-117.73549939999998",
  "maxDistance": 0
}

And Response is

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
public class SearchKivi implements Parcelable {
    @SerializedName("device_id") private String device_id;

    @SerializedName("lat") private String lat;

    @SerializedName("lng") private String lng;

    @SerializedName("limit") private int limit;

    @SerializedName("maxDistance") private int maxDistance;

    public SearchKivi(String device_id, String lat, String lng, int limit, int maxDistance) {
        this.device_id = device_id;
        this.lat = lat;
        this.lng = lng;
        this.limit = limit;
        this.maxDistance = maxDistance;
    }

    protected SearchKivi(Parcel in) {
        device_id = in.readString();
        lat = in.readString();
        lng = in.readString();
        limit = in.readInt();
        maxDistance = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(device_id);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeInt(limit);
        dest.writeInt(maxDistance);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<SearchKivi> CREATOR =
            new Parcelable.Creator<SearchKivi>() {
                @Override public SearchKivi createFromParcel(Parcel in) {
                    return new SearchKivi(in);
                }

                @Override public SearchKivi[] newArray(int size) {
                    return new SearchKivi[size];
                }
            };
}