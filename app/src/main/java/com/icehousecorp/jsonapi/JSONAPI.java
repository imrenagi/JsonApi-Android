package com.icehousecorp.jsonapi;

import com.icehousecorp.jsonapi.constant.JSONAPIMemberKey;
import com.icehousecorp.jsonapi.constant.JSONAPIResourceKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by zendy on 2/10/15.
 */
public class JSONAPI {

    private JSONAPILinkedResource jsonapiLinkedResource;
    private JSONAPILinks jsonapiLinks;

    public Object generateUsableObject(String jsonString, Class targetClass)
            throws JSONException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {

        JSONObject jsonObject = new JSONObject(jsonString);

        jsonapiLinkedResource = new JSONAPILinkedResource(jsonObject.getJSONObject(JSONAPIMemberKey.LINKED_MEMBER));
        jsonapiLinks = new JSONAPILinks(jsonObject);

        return parse(jsonObject, targetClass);
    }

    private Object parse(JSONObject aJSONObject, Class toTargetClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException, JSONException {
        Object targetObject = toTargetClass.getConstructor().newInstance();

        HashMap<String, Field> fieldMap = JSONAPIMapper.createFieldMap(toTargetClass);

        Iterator<String> jsonIterator = aJSONObject.keys();

        while (jsonIterator.hasNext()) {
            String jsonMemberKey = jsonIterator.next();

            if (JSONAPILinks.isLinks(jsonMemberKey)) {

                JSONObject linksJsonObject = ((JSONObject) aJSONObject.get(jsonMemberKey)); //get the json object of a links member

                // iterator for all field in links json
                Iterator<String> linksIterator = linksJsonObject.keys();

                while (linksIterator.hasNext()) {

                    String linksKey = linksIterator.next(); //get the key inside links

                    Field fieldForKey = fieldMap.get(linksKey);
                    if (fieldForKey != null) {

                        Object resourceFromLink = linksJsonObject.get(linksKey);
                        String linksResourceType = jsonapiLinks.getType(resourceFromLink, linksKey);

                        Object resourceId = jsonapiLinks.findResourceId(linksJsonObject.get(linksKey));

                        if (resourceId instanceof JSONArray && fieldForKey.getType().isArray()) {

                            JSONArray arrayOfResourceId = (JSONArray) resourceId;
                            Object arrayOfResource = Array.newInstance(fieldForKey.getType().getComponentType(), arrayOfResourceId.length());
                            for (int i = 0; i < arrayOfResourceId.length(); i++) {
                                JSONObject jsonResource = jsonapiLinkedResource.getAResourceObject(linksResourceType, (String) arrayOfResourceId.get(i));
                                ((Object[]) arrayOfResource)[i] = parse(jsonResource, fieldForKey.getType().getComponentType());
                            }
                            JSONAPIMapper.setFieldValue(targetObject, fieldForKey, arrayOfResource);

                        } else if (resourceId instanceof String) {

                            JSONObject jsonResource = jsonapiLinkedResource.getAResourceObject(linksResourceType, (String) resourceId);
                            Object resource = parse(jsonResource, fieldForKey.getType());
                            JSONAPIMapper.setFieldValue(targetObject, fieldForKey, resource);

                        } else if (resourceId == null) {

                            if (!fieldForKey.getType().isArray()) {
                                Object resource = parse(linksJsonObject.getJSONObject(linksKey), fieldForKey.getType());
                                JSONAPIMapper.setFieldValue(targetObject, fieldForKey, resource);
                            }
                        }
                    }
                }

            } else {

                Field fieldForKey = fieldMap.get(jsonMemberKey);

                if (fieldForKey != null) {
                    Object jsonObjectKey = aJSONObject.get(jsonMemberKey);
                    if (jsonObjectKey instanceof JSONArray && fieldForKey.getType().isArray()) {

                        JSONArray jsonArray = (JSONArray) jsonObjectKey;
                        Object arrayObject = Array.newInstance(fieldForKey.getType().getComponentType(), jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {

                            Object objectFromArray = jsonArray.get(i);
                            if (objectFromArray instanceof JSONObject) {
                                Object parsedObject = parse(((JSONObject) objectFromArray), fieldForKey.getType().getComponentType());
                                ((Object[]) arrayObject)[i] = parsedObject;
                            } else {
                                ((Object[]) arrayObject)[i] = objectFromArray;
                            }
                        }

                        JSONAPIMapper.setFieldValue(targetObject, fieldForKey, arrayObject);

                    } else if (jsonObjectKey instanceof JSONObject) {

                        Object parsedObject = parse((JSONObject) jsonObjectKey, fieldForKey.getType());
                        JSONAPIMapper.setFieldValue(targetObject, fieldForKey, parsedObject);

                    } else {

                        JSONAPIMapper.setFieldValue(targetObject, fieldForKey, jsonObjectKey);

                    }
                }

            }
        }
        return targetObject;
    }

}
