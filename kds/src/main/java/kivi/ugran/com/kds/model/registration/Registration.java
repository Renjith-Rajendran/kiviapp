package kivi.ugran.com.kds.model.registration;

import com.google.gson.annotations.SerializedName;

/*
   Refer registration.json
 */
public class Registration {
    @SerializedName("coordinates") Coordinates coordinates;

    @SerializedName("connectivity_type") private String connectivityType;

    @SerializedName("device_id") private String deviceId;

    @SerializedName("email") private String email;

    @SerializedName("dob") private String dob;

    @SerializedName("isUpdated") private int isUpdated;

    @SerializedName("kivi_available_rating") private int availableRating;

    @SerializedName("kivi_available_rating_count") private int availableRatingCount;

    @SerializedName("kivi_content_rating") private int contentRating;

    @SerializedName("kivi_content_rating_count") private int contentRatingCount;

    @SerializedName("kivi_camera_resoluition") private String cameraResolution;

    @SerializedName("kivi_device_name") private String deviceName;

    @SerializedName("kivi_device_token") private String deviceToken;

    @SerializedName("device_type") private String deviceType;

    @SerializedName("kivi_id") private String id;

    @SerializedName("kivi_name") private String name;

    @SerializedName("kivi_requestor_rating") private int requestorRating;

    @SerializedName("kivi_requestor_rating_count") private int requestorRatingCount;

    @SerializedName("kivi_rest_time") private int restTime;

    @SerializedName("kivi_status") private String status;

    @SerializedName("kivi_system_version") private String systemVersion;

    @SerializedName("kivi_type") private Type type;

    @SerializedName("network_type") private String networkType;

    @SerializedName("notification_sound") private int notificationSound;

    @SerializedName("notification_vibration") private int notificationVibration;

    @SerializedName("profile_pic") private String profilePic;

    @SerializedName("user_roles") private Roles roles;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @SerializedName("kivi_language") Language language;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getConnectivityType() {
        return connectivityType;
    }

    public void setConnectivityType(String connectivityType) {
        this.connectivityType = connectivityType;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(int isUpdated) {
        this.isUpdated = isUpdated;
    }

    public int getAvailableRating() {
        return availableRating;
    }

    public void setAvailableRating(int availableRating) {
        this.availableRating = availableRating;
    }

    public int getAvailableRatingCount() {
        return availableRatingCount;
    }

    public void setAvailableRatingCount(int availableRatingCount) {
        this.availableRatingCount = availableRatingCount;
    }

    public int getContentRating() {
        return contentRating;
    }

    public void setContentRating(int contentRating) {
        this.contentRating = contentRating;
    }

    public int getContentRatingCount() {
        return contentRatingCount;
    }

    public void setContentRatingCount(int contentRatingCount) {
        this.contentRatingCount = contentRatingCount;
    }

    public String getCameraResolution() {
        return cameraResolution;
    }

    public void setCameraResolution(String cameraResolution) {
        this.cameraResolution = cameraResolution;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequestorRating() {
        return requestorRating;
    }

    public void setRequestorRating(int requestorRating) {
        this.requestorRating = requestorRating;
    }

    public int getRequestorRatingCount() {
        return requestorRatingCount;
    }

    public void setRequestorRatingCount(int requestorRatingCount) {
        this.requestorRatingCount = requestorRatingCount;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public int getNotificationSound() {
        return notificationSound;
    }

    public void setNotificationSound(int notificationSound) {
        this.notificationSound = notificationSound;
    }

    public int getNotificationVibration() {
        return notificationVibration;
    }

    public void setNotificationVibration(int notificationVibration) {
        this.notificationVibration = notificationVibration;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public void initData() {
        this.connectivityType = "GPS";

        this.deviceId = "";

        this.email = "";

        this.dob = "";

        this.isUpdated = 0;

        this.availableRating = 0;

        this.availableRatingCount = 0;

        this.contentRating = 0;

        this.contentRatingCount = 0;

        this.cameraResolution = "320*480";

        this.deviceName = "";

        this.deviceToken = "";

        this.id = "";

        this.name = "";

        this.requestorRating = 0;

        this.requestorRatingCount = 0;

        this.restTime = 10;

        this.status = "Available";

        this.systemVersion = "9.3.5";

        this.networkType = "Wi-Fi";

        this.notificationSound = 1;

        this.notificationVibration = 1;

        this.profilePic = "";

        //nested classes
        this.roles = new Roles("0", 1);
        this.type = new Type(1, 1);
        //this.blackouts = new Blackouts("","","","","","","","");
        this.language = new Language("English");
        this.coordinates = new Coordinates(0.0, 0.0);
    }
}
