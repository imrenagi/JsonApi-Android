package com.icehousecorp.jsonapi.sample.model;

import com.icehousecorp.jsonapi.annotation.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class Movie {
    private String id;
    private String contentType;
    @SerializeName("info")
    private MovieInfo movieInfo;
}
