package com.icehousecorp.jsonapi.sample.model;

import com.icehousecorp.jsonapi.JSONAPIResourceObject;
import com.icehousecorp.jsonapi.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class Movie extends JSONAPIResourceObject {

    private String contentType;
    @SerializeName("info")
    private MovieInfo movieInfo;
}
