package model;

import com.example.zendy.jsonapi.SerializeName;

/**
 * Created by zendy on 2/10/15.
 */
public class DataResponse {
    @SerializeName("data")
    private RowData mRowData[];
}
