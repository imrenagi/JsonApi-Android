package com.example.zendy.jsonapi;

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
public class JsonApi {

    private JSONObject jsonLinksRoot;

    private HashMap<String, HashMap<String, JSONObject>> linkedMap;

    public Object fromJson(String json, Class targetClass)
            throws JSONException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        JSONObject jsonObject = new JSONObject(json);
        jsonLinksRoot = jsonObject.getJSONObject(JsonApiKey.LINKS);
        linkedMap = generateMapLinkedResource(jsonObject.getJSONObject(JsonApiKey.LINKED));
        return parse(jsonObject, targetClass);
    }

    private Object parse(JSONObject jsonObject, Class targetClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException, JSONException {
        Object targetObject = targetClass.getConstructor().newInstance();
        HashMap<String, Field> fieldMap = createFiledMap(targetClass);
        Iterator<String> jsonIterator = jsonObject.keys();
        while (jsonIterator.hasNext()) {
            String key = jsonIterator.next();
            if (isLinks(key)) {
                JSONObject linksJsonObject = ((JSONObject) jsonObject.get(key));
                Iterator<String> linksIterator = linksJsonObject.keys();
                while (linksIterator.hasNext()) {
                    String linksKey = linksIterator.next();
                    Field fieldForKey = fieldMap.get(linksKey);
                    if (fieldForKey != null) {
                        Object resourceFromLink=linksJsonObject.get(linksKey);
                        String linksResourceType ="";
                        if(resourceFromLink instanceof JSONObject) {
                            linksResourceType =
                                    getResourceType((JSONObject) linksJsonObject.get(linksKey),
                                            linksKey).equals("")
                                            ? linksKey : getResourceType(
                                            (JSONObject) linksJsonObject.get(linksKey), linksKey);
                        }else if(resourceFromLink instanceof JSONArray){
                            linksResourceType=getResourceTypeFromRootLinks(linksKey);
                        }
                            JSONArray arrayOfResourceId = findArrayResourceId(
                                linksJsonObject.get(linksKey));
                        if (arrayOfResourceId != null && fieldForKey.getType().isArray()) {
                            Object arrayOfResource = Array
                                    .newInstance(fieldForKey.getType().getComponentType(),
                                            arrayOfResourceId.length());
                            for (int i = 0; i < arrayOfResourceId.length(); i++) {
                                JSONObject jsonResource = linkedMap.get(linksResourceType)
                                        .get(arrayOfResourceId.get(i));
                                ((Object[]) arrayOfResource)[i] = parse(jsonResource,
                                        fieldForKey.getType().getComponentType());
                            }
                            setFieldValue(targetObject, fieldForKey, arrayOfResource);
                        } else {
                            JSONObject jsonResource = linkedMap.get(linksResourceType)
                                    .get(linksJsonObject.getString(linksResourceType));
                            Object resource = parse(jsonResource, fieldForKey.getType());
                            setFieldValue(targetObject, fieldForKey, resource);
                        }
                    }
                }
            } else {
                Field fieldForKey = fieldMap.get(key);
                if (fieldForKey != null) {
                    Object jsonObjectKey = jsonObject.get(key);
                    if (jsonObjectKey instanceof JSONArray && fieldForKey.getType().isArray()) {
                        JSONArray jsonArray = (JSONArray) jsonObjectKey;
                        Object arrayObject = Array
                                .newInstance(fieldForKey.getType().getComponentType(),
                                        jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Object objectFromArray = jsonArray.get(i);
                            if (objectFromArray instanceof JSONObject) {
                                Object parsedObject = parse(((JSONObject) objectFromArray),
                                        fieldForKey.getType().getComponentType());
                                ((Object[]) arrayObject)[i] = parsedObject;
                            } else {
                                ((Object[]) arrayObject)[i] = objectFromArray;
                            }
                        }
                        setFieldValue(targetObject, fieldForKey, arrayObject);
                    } else if (jsonObjectKey instanceof JSONObject) {
                        Object parsedObject = parse((JSONObject) jsonObjectKey,
                                fieldForKey.getType());
                        setFieldValue(targetObject, fieldForKey, parsedObject);
                    } else {
                        setFieldValue(targetObject, fieldForKey, jsonObjectKey);
                    }
                }
            }
        }
        return targetObject;
    }

    private HashMap<String, Field> createFiledMap(Class classType) {
        HashMap<String, Field> map = new HashMap<>();
        Field fields[] = classType.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getAnnotation(SerializeName.class) != null ? field
                    .getAnnotation(SerializeName.class).value() : field.getName();
            map.put(fieldName, field);
        }
        return map;
    }

    private boolean isLinks(String key) {
        return key.equals(JsonApiKey.LINKS);
    }

    private void setFieldValue(Object target, Field field, Object value)
            throws IllegalAccessException {
        field.setAccessible(true);
        field.set(target, field.getType().cast(value));
    }

    private HashMap<String, HashMap<String, JSONObject>> generateMapLinkedResource(
            JSONObject linkedObject)
            throws JSONException {
        HashMap<String, HashMap<String, JSONObject>> linkedMap = new HashMap<>();
        Iterator<String> iterator = linkedObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object object = linkedObject.get(key);
            if (object instanceof JSONArray) {
                HashMap<String, JSONObject> objectMap = new HashMap<>();
                JSONArray linkedArray = (JSONArray) object;
                for (int i = 0; i < linkedArray.length(); i++) {
                    Object objectFromArray = linkedArray.get(i);
                    if (objectFromArray instanceof JSONObject) {
                        JSONObject jsonObjectFromArray = (JSONObject) objectFromArray;
                        String id = jsonObjectFromArray.getString(JsonApiKey.ID);
                        objectMap.put(id, jsonObjectFromArray);
                    }
                }
                linkedMap.put(key, objectMap);
            }
        }
        return linkedMap;
    }

    private String getResourceTypeFromRootLinks(String term) {
        Iterator<String> iterator = jsonLinksRoot.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains(term)) {
                try {
                    return ((JSONObject) jsonLinksRoot.get(key)).getString(JsonApiKey.TYPE);
                } catch (JSONException e) {
                    return "";
                }
            }
        }
        return "";
    }

    private String getResourceTypeFromResourceObject(JSONObject resourceObject) {
        try {
            String type = resourceObject.getString(JsonApiKey.TYPE);
            return type;
        } catch (JSONException e) {
            return "";
        }
    }

    private JSONArray findArrayResourceId(Object object) throws JSONException {
        if (object instanceof JSONArray) {
            return (JSONArray) object;
        } else if (object instanceof JSONObject) {
            if (((JSONObject) object).get(JsonApiKey.IDS) instanceof JSONArray) {
                return (JSONArray) ((JSONObject) object).get(JsonApiKey.IDS);
            }
        }
        return null;
    }

    private String getResourceType(JSONObject resourceObject, String key) {
        return getResourceTypeFromRootLinks(key).equals("") ? (
                getResourceTypeFromResourceObject(resourceObject).equals("") ? key
                        : getResourceTypeFromResourceObject(resourceObject)) : getResourceTypeFromRootLinks(key);
    }

}
