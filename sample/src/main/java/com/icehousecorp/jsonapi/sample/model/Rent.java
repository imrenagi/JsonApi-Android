package com.icehousecorp.jsonapi.sample.model;

import com.icehousecorp.jsonapi.annotation.SerializeName;

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
