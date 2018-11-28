package kivi.ugran.com.firebase.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/*
“notification” : {

“body” : “great match!”,

“title” : “Portugal vs. Denmark”,

“icon” : “myicon”,

“sound” : “mySound”

}
 */
public class Notification implements Parcelable {
    @SerializedName("body")
    private String body;

    @SerializedName("title")
    private String title;

    @SerializedName("icon")
    private String icon;

    @SerializedName("sound")
    private String sound;

    public Notification(String body, String title, String icon, String sound) {
        this.body = body;
        this.title = title;
        this.icon = icon;
        this.sound = sound;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    protected Notification(Parcel in) {
        body = in.readString();
        title = in.readString();
        icon = in.readString();
        sound = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeString(title);
        dest.writeString(icon);
        dest.writeString(sound);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
