package model;

import com.example.zendy.jsonapi.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class WishListData {
    @SerializeName("movie")
    private Movie movies[];
    private Tv tv[];
}
