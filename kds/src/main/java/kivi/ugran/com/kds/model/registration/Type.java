package kivi.ugran.com.kds.model.registration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/*
  ""kivi_type"": {
  ""photo"": 1,
  ""video"": 1
},
 */

public class Type implements Parcelable {
    @SerializedName("photo") private int photo;
    @SerializedName("video") private int video;

    public Type(int photo, int video) {
        this.photo = photo;
        this.video = video;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    protected Type(Parcel in) {
        photo = in.readInt();
        video = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photo);
        dest.writeInt(video);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Type> CREATOR = new Parcelable.Creator<Type>() {
        @Override public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override public Type[] newArray(int size) {
            return new Type[size];
        }
    };
}
