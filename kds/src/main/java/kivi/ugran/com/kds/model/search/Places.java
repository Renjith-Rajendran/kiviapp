package kivi.ugran.com.kds.model.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Places {
    @SerializedName("places") private List<Place> places;

    public List<Place> getPlaces() {
        return places;
    }
}
