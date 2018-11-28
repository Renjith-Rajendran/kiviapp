package kivi.ugran.com.kds.model;

import com.google.gson.annotations.SerializedName;

/*
{
	"kivi_request_id":"Hello123",
	"device_id": "testy2",
	"requestor_device_id": "deviceid",
	"kivi_notification_type":"rateKivi",
	"kivi_comment":"Comment for Session",
	"kivi_rating":5
}

Error response is

{
    "title": null,
    "Err": "Error in kivi data found",
    "request": "failed"
}

Success response is

{
    "title": Success,
    "request": "success"
}

 */

public class KiviRating {
    @SerializedName("device_id") private String device_id;

    @SerializedName("kivi_request_id") private String kiviRequestId;

    @SerializedName("requestor_device_id") private String requestorDeviceId;

    @SerializedName("kivi_notification_type") private String kiviNotificationType;

    @SerializedName("kivi_comment") private String kiviComment;

    @SerializedName("kivi_rating") private int kiviRating;

    public KiviRating(String device_id, String kiviRequestId, String requestorDeviceId, String kiviNotificationType,
            String kiviComment, int kiviRating) {
        this.device_id = device_id;
        this.kiviRequestId = kiviRequestId;
        this.requestorDeviceId = requestorDeviceId;
        this.kiviNotificationType = kiviNotificationType;
        this.kiviComment = kiviComment;
        this.kiviRating = kiviRating;
    }
}