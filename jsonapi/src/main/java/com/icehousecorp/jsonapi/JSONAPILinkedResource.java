package com.icehousecorp.jsonapi;

import com.icehousecorp.jsonapi.constant.JSONAPIResourceKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is used to create an instance of "linked" object of a JSON API Format.
 * <p/>
 * Created by imrenagi on 2/13/15.
 */
public class JSONAPILinkedResource {

    /**
     * HashMap containing all object inside "linked" object
     */
    private HashMap<String, HashMap<String, JSONObject>> linkedMap;

    /**
     * @param type the type of JSON API linked object
     * @param id   id of a resource object
     * @return JSONObject of a particular object inside a type who has id.
     */
    public JSONObject getAResourceObject(String type, String id) {
        return linkedMap.get(type).get(id);
    }

    /**
     * @param linkedObject JSONObject from "linked"
     * @throws JSONException
     */
    public JSONAPILinkedResource(JSONObject linkedObject) throws JSONException {
        generateLinkedMap(linkedObject);
    }

    /**
     * Generate a HashMap containing all value inside "linked" key of a JSON API Object
     *
     * @param linkedObject JSONObject from linked member
     */
    private void generateLinkedMap(JSONObject linkedObject) throws JSONException {
        linkedMap = new HashMap<>();

        Iterator<String> iterator = linkedObject.keys();

        while (iterator.hasNext()) {

            String linkedKey = iterator.next();
            Object collectionOfATypeResourceObject = linkedObject.get(linkedKey);

            if (collectionOfATypeResourceObject instanceof JSONArray) {
                HashMap<String, JSONObject> objectMap = new HashMap<>();
                JSONArray resourceObjectCollections = (JSONArray) collectionOfATypeResourceObject;

                for (int i = 0; i < resourceObjectCollections.length(); i++) {

                    Object resourceObject = resourceObjectCollections.get(i);

                    if (resourceObject instanceof JSONObject) {
                        JSONObject jsonResourceObject = (JSONObject) resourceObject;
                        String id = jsonResourceObject.getString(JSONAPIResourceKey.ID_KEY);
                        objectMap.put(id, jsonResourceObject);
                    }
                }

                linkedMap.put(linkedKey, objectMap);
            }
        }
    }

}
