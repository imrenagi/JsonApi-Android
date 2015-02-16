package model;

import com.icehousecorp.jsonapi.Annotation.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class MovieInfo {
    private String title;
    @SerializeName("releaseYear")
    private String release;
    private String rating;
    private String length;
    private Genre genres[];
    private People actors[];
    private Pricing pricing;
    private Assets assets;
}
