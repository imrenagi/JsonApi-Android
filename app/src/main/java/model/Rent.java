package model;

import com.icehousecorp.jsonapi.Annotation.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class Rent {
    @SerializeName("HDX")
    private String hdx;
    @SerializeName("HD")
    private String hd;
    @SerializeName("SD")
    private String sd;
}
