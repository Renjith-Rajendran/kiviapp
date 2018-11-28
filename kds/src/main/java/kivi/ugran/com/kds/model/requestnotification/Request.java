package kivi.ugran.com.kds.model.requestnotification;

import com.google.gson.annotations.SerializedName;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.kds.model.registration.Coordinates;
import kivi.ugran.com.kds.model.search.Place;

public class Request {
    @SerializedName("device_id") private String deviceId;

    @SerializedName("requestor_device_id") private String requestorDeviceId;

    @SerializedName("coordinates") private Coordinates coordinates;

    @SerializedName("request_location") private String requestLocation;

    @SerializedName("request_text") private String requestText;

    @SerializedName("kivi_rating") private int kiviRating;

    @SerializedName("kivi_comment") private String kiviComment;

    @SerializedName("kivi_request_speed") private String kiviRequestSpeed;

    @SerializedName("request_type") private String requestType;

    @SerializedName("createdAt") private String createdAt;

    @SerializedName("updatedAt") private String updatedAt;

    @SerializedName("request_status") private String requestStatus;

    @SerializedName("kivi_notification_type") private String kiviNotificationType;

    @SerializedName("request_content") private String requestContent;

    @SerializedName("request_contentType") private String requestContentType;

    @SerializedName("request_response") private Boolean requestResponse;

    @SerializedName("requestor_kivi_name") private String requestorKiviName;

    //This is request ID returned in the response
    @SerializedName("id") private String id;

    public Request() {
    }

    public Request getRequestData(Place place, String location, String requestType, String kiviRequestSpeed, String requestStatus, String requestText, Boolean requestResponse) {
        setDeviceId(KiviApplication.getAndroidDeviceID())
                .setKiviRating(place.getAvailableRating())
                .setRequestorDeviceId(place.getDeviceId())
                .setCoordinates(place.getCoordinates())
                .setRequestLocation(location)
                .setRequestType(requestType)
                .setKiviRequestSpeed(kiviRequestSpeed)
                .setRequestStatus(requestStatus)
                .setRequestText(requestText)
                .setRequestResponse(requestResponse);
                return this;
     }

    public String getDeviceId() {
        return deviceId;
    }

    public Request setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getRequestorDeviceId() {
        return requestorDeviceId;
    }

    public Request setRequestorDeviceId(String requestorDeviceId) {
        this.requestorDeviceId = requestorDeviceId;
        return this;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Request setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public String getRequestLocation() {
        return requestLocation;
    }

    public Request setRequestLocation(String requestLocation) {
        this.requestLocation = requestLocation;
        return this;
    }

    public String getRequestText() {
        return requestText;
    }

    public Request setRequestText(String requestText) {
        this.requestText = requestText;
        return this;
    }

    public int getKiviRating() {
        return kiviRating;
    }

    public Request setKiviRating(int kiviRating) {
        this.kiviRating = kiviRating;
        return this;
    }

    public String getKiviComment() {
        return kiviComment;
    }

    public Request setKiviComment(String kiviComment) {
        this.kiviComment = kiviComment;
        return this;
    }

    public String getKiviRequestSpeed() {
        return kiviRequestSpeed;
    }

    public Request setKiviRequestSpeed(String kiviRequestSpeed) {
        this.kiviRequestSpeed = kiviRequestSpeed;
        return this;
    }

    public String getRequestType() {
        return requestType;
    }

    public Request setRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Request setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Request setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public Request setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public String getKiviNotificationType() {
        return kiviNotificationType;
    }

    public Request setKiviNotificationType(String kiviNotificationType) {
        this.kiviNotificationType = kiviNotificationType;
        return this;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public Request setRequestContent(String requestContent) {
        this.requestContent = requestContent;
        return this;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public Request setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
        return this;
    }

    public Boolean getRequestResponse() {
        return requestResponse;
    }

    public Request setRequestResponse(Boolean requestResponse) {
        this.requestResponse = requestResponse;
        return this;
    }

    public String getRequestorKiviName() {
        return requestorKiviName;
    }

    public Request setRequestorKiviName(String requestorKiviName) {
        this.requestorKiviName = requestorKiviName;
        return this;
    }

    public String getId() {
        return id;
    }

    private Request setId(String id) {
        this.id = id;
        return this;
    }
}
