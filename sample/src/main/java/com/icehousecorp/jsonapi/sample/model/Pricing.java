package com.icehousecorp.jsonapi.sample.model;

import com.icehousecorp.jsonapi.JSONAPIResourceObject;

/**
 * Created by zendy on 2/10/15.
 */
public class Pricing extends JSONAPIResourceObject {

    private String currency;
    private Buy buy;
    private Rent rent;
}
