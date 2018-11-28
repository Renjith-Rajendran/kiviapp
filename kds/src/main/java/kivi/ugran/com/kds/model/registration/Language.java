package kivi.ugran.com.kds.model.registration;

import android.os.Parcel;
import android.os.Parcelable;

/*
  ""kivi_language"": [
  ""English""
 */
public class Language implements Parcelable {
    private String language;

    public Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    protected Language(Parcel in) {
        language = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(language);
    }

    @SuppressWarnings("unused") public static final Parcelable.Creator<Language> CREATOR =
            new Parcelable.Creator<Language>() {
                @Override public Language createFromParcel(Parcel in) {
                    return new Language(in);
                }

                @Override public Language[] newArray(int size) {
                    return new Language[size];
                }
            };
}
