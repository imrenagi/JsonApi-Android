package model;

import com.example.zendy.jsonapi.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class Buy {
    @SerializeName("HDX")
    private String hdx;
    @SerializeName("HD")
    private String hd;
    @SerializeName("SD")
    private String sd;
}
