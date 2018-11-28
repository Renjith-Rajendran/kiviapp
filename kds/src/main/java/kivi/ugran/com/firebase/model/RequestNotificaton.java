package kivi.ugran.com.firebase.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/*
   {

“to” : “bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1…”,

“notification” : {

“body” : “great match!”,

“title” : “Portugal vs. Denmark”,

“icon” : “myicon”,

“sound” : “mySound”

}

}
 */

public class RequestNotificaton implements Parcelable {
    @SerializedName("to")
    private String token;

    @SerializedName("notification")
    private Notification notification;

    public RequestNotificaton(String to, Notification notification) {
        this.token = to;
        this.notification = notification;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    protected RequestNotificaton(Parcel in) {
        token = in.readString();
        notification = (Notification) in.readValue(Notification.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token);
        dest.writeValue(notification);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RequestNotificaton> CREATOR = new Parcelable.Creator<RequestNotificaton>() {
        @Override
        public RequestNotificaton createFromParcel(Parcel in) {
            return new RequestNotificaton(in);
        }

        @Override
        public RequestNotificaton[] newArray(int size) {
            return new RequestNotificaton[size];
        }
    };
}
