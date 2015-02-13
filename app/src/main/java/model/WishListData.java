package model;

import com.icehousecorp.jsonapi.Annotation.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class WishListData {
    @SerializeName("movie")
    private Movie movies;
    private Tv tv[];
}
