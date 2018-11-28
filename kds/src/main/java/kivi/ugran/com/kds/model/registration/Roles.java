package kivi.ugran.com.kds.model.registration;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/*
     ""user_roles"": {
  ""admin"": ""0"",
  ""user"": 1
}
 */
public class Roles implements Parcelable {

    @SerializedName("admin") private String admin;

    @SerializedName("user") private int user;

    public Roles(String admin, int user) {
        this.admin = admin;
        this.user = user;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    protected Roles(Parcel in) {
        admin = in.readString();
        user = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(admin);
        dest.writeInt(user);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Roles> CREATOR =
            new Parcelable.Creator<Roles>() {
                @Override public Roles createFromParcel(Parcel in) {
                    return new Roles(in);
                }

                @Override public Roles[] newArray(int size) {
                    return new Roles[size];
                }
            };
}