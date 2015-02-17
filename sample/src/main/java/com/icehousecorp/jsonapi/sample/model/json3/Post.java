package com.icehousecorp.jsonapi.sample.model.json3;

import com.icehousecorp.jsonapi.JSONAPIResourceObject;

/**
 * Created by imre on 2/11/15.
 */
public class Post extends JSONAPIResourceObject {

    private String title;
    private People author;
    private Comment[] comments;

}
