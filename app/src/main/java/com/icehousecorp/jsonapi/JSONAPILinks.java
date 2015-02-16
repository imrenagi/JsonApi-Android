package com.icehousecorp.jsonapi;

import com.icehousecorp.jsonapi.constant.JSONAPIMemberKey;
import com.icehousecorp.jsonapi.constant.JSONAPIResourceKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by imrenagi on 2/13/15.
 */
public class JSONAPILinks {

    private final static String EMPTY_STRING = "";

    private JSONObject links;

    /**
     * Default Constructor
     *
     * @param jsonObject raw JSON API Object
     * @throws JSONException
     */
    public JSONAPILinks(JSONObject jsonObject) throws JSONException {
        links = jsonObject.getJSONObject(JSONAPIMemberKey.LINKS_MEMBER);
    }

    public String getType(Object object, String linksKey) {
        String type;
        if (object instanceof JSONObject) {
            type = getResourceTypeForJSONObject((JSONObject) object, linksKey);
        } else {
            type = getResourceTypeFromLinksMember(linksKey);
        }

        if (type.equals(EMPTY_STRING)) {
            return linksKey;
        } else {
            return type;
        }
    }

    /**
     * If a JSON API formatted Object give information about "links",
     * this function will return URI Template in String format inside term field.
     *
     * @param term
     * @return type of a resource object in String
     */
    private String getResourceTypeFromLinksMember(String term) {
        Iterator<String> iterator = links.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains(term)) {
                try {
                    return ((JSONObject) links.get(key)).getString(JSONAPIResourceKey.TYPE_KEY);
                } catch (JSONException e) {
                    return EMPTY_STRING;
                }
            }
        }
        return EMPTY_STRING;
    }

    private String getResourceTypeForJSONObject(JSONObject resourceObject, String key) {

        String typeFromLinksMember = getResourceTypeFromLinksMember(key);
        String typeFromResourceObject = getResourceTypeFromResourceObject(resourceObject);

        if (typeFromLinksMember.equals(EMPTY_STRING)) {
            if (typeFromResourceObject.equals(EMPTY_STRING)) {
                return key;
            } else {
                return typeFromResourceObject;
            }
        } else {
            return typeFromLinksMember;
        }
    }

    /**
     * @param resourceObject
     * @return type of a resource object in String if the type is specified in the resource object
     */
    private String getResourceTypeFromResourceObject(JSONObject resourceObject) {
        try {
            String type = resourceObject.getString(JSONAPIResourceKey.TYPE_KEY);
            return type;
        } catch (JSONException e) {
            return EMPTY_STRING;
        }
    }

    /**
     * If a JSON API formatted Object give information about "links",
     * this function will return URI Template in String format inside term field.
     *
     * @param term
     * @return URI Template in String
     */
    private String getURLTemplateFromLinksMember(String term) {
        Iterator<String> iterator = links.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains(term)) {
                try {
                    return ((JSONObject) links.get(key)).getString(JSONAPIResourceKey.HREF_KEY);
                } catch (JSONException e) {
                    return EMPTY_STRING;
                }
            }
        }
        return EMPTY_STRING;
    }

    /**
     * @param key
     * @return true if key is "links"
     */
    public static boolean isLinks(String key) {
        return key.equals(JSONAPIMemberKey.LINKS_MEMBER);
    }

    public Object findResourceId(Object linkedResource) {
        if (linkedResource instanceof JSONArray) {
            return linkedResource;
        } else if (linkedResource instanceof JSONObject) {
            try {
                if (((JSONObject) linkedResource).has(JSONAPIResourceKey.IDS_KEY)) {
                    if (((JSONObject) linkedResource).get(JSONAPIResourceKey.IDS_KEY) instanceof JSONArray) {
                        return ((JSONObject) linkedResource).getJSONArray(JSONAPIResourceKey.IDS_KEY);
                    }
                } else if (((JSONObject) linkedResource).has(JSONAPIResourceKey.ID_KEY)) {
                    return ((JSONObject) linkedResource).get(JSONAPIResourceKey.ID_KEY);
                }
            } catch (JSONException e) {
            }
        } else if (linkedResource instanceof String) {
            return linkedResource;
        }
        return null;
    }
}
