package com.icehousecorp.jsonapi;

import com.icehousecorp.jsonapi.constant.JSONAPIMemberKey;
import com.icehousecorp.jsonapi.constant.JSONAPIResourceKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.util.Iterator;

/**
 * Created by imrenagi on 2/13/15.
 */
public class JSONAPILinks {

    private final static String EMPTY_STRING = "";

    private JSONObject links;

    /**
     * @param key
     * @return true if key is "links"
     */
    public static boolean isLinks(String key) {
        return key.equals(JSONAPIMemberKey.LINKS_MEMBER);
    }

    /**
     * Default Constructor
     *
     * @param jsonObject raw JSON API Object
     * @throws JSONException
     */
    public JSONAPILinks(JSONObject jsonObject) throws JSONException {
        try {
            links = jsonObject.getJSONObject(JSONAPIMemberKey.LINKS_MEMBER);
        } catch (JSONException e) {
            JSONException exception = new JSONException(
                    "Can not find links key or Links should be type of JSONObject");
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
    }

    /**
     * @param resourceObject
     * @param linksKey
     * @return the type of a resource object.
     */
    public String getType(Object resourceObject, String linksKey) {
        String type;
        if (resourceObject instanceof JSONObject) {
            type = getResourceTypeForJSONObject((JSONObject) resourceObject, linksKey);
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
     * @param keyword
     * @return linked type of a resource object
     */
    private String getResourceTypeFromLinksMember(String keyword) {
        Iterator<String> iterator = links.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains(keyword)) {
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
            return typeFromResourceObject.equals(EMPTY_STRING) ? key : typeFromResourceObject;
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
            return resourceObject.getString(JSONAPIResourceKey.TYPE_KEY);
        } catch (JSONException e) {
            return EMPTY_STRING;
        }
    }

    /**
     * @param resourceObject resource resourceObject class
     * @param linksKey
     * @return
     */
    public String getURLTemplate(Object resourceObject, String linksKey) {
        String type;
        if (resourceObject instanceof JSONObject) {
            type = getURLTemplateForJSONObject((JSONObject) resourceObject, linksKey);
        } else {
            type = getURLTemplateFromLinksMember(linksKey);
        }

        return type;
    }

    private String getURLTemplateForJSONObject(JSONObject resourceObject, String key) {

        String hrefFromLinksMember = getURLTemplateFromLinksMember(key);
        String hrefFromResourceObject = getURLTemplateFromResourceObject(resourceObject);

        if (hrefFromLinksMember == null) {
            return hrefFromResourceObject;
        } else {
            return hrefFromLinksMember;
        }
    }

    /**
     * Get URL Template/href link for a resource object specified in href field within itself.
     *
     * @param resourceObject
     * @return URL Template / href links
     */
    private String getURLTemplateFromResourceObject(JSONObject resourceObject) {
        try {
            return resourceObject.getString(JSONAPIResourceKey.HREF_KEY);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * If a JSON API formatted Object give information about "links",
     * this function will return URL Template in String format inside term field.
     *
     * @param term
     * @return URI Template in String
     */
    public String getURLTemplateFromLinksMember(String term) {
        Iterator<String> iterator = links.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains(term)) {
                try {
                    Object linksObject = links.get(key);
                    if (linksObject instanceof JSONObject && ((JSONObject) linksObject).has(
                            JSONAPIResourceKey.HREF_KEY)) {
                        return ((JSONObject) linksObject).getString(JSONAPIResourceKey.HREF_KEY);
                    } else if (linksObject instanceof String) {
                        return (String) linksObject;
                    }
                } catch (JSONException e) {
                    return null;
                }
            }
        }
        return null;
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
                }else{
                    throw new JSONException("Can not find resource id. This object will be handled not as linked resource");
                }
            } catch (JSONException e) {
                Log.e("JSONAPI",e.getMessage());
            }
        } else if (linkedResource instanceof String) {
            return linkedResource;
        }
        return null;
    }

    /**
     * @param term
     * @return the json field containing term
     */
    public String getLinksKeyContainingTerm(String term) {

        Iterator<String> iterator = links.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains(term)) {
                return key;
            }
        }
        return EMPTY_STRING;
    }
}
