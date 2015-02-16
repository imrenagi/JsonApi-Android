package com.icehousecorp.jsonapi;

import com.icehousecorp.jsonapi.annotation.SerializeName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by imrenagi on 2/16/15.
 */
public class JSONAPIMapper {

    /**
     * @param classType
     * @return HashMap for fieldName(key) and Field(value)
     */
    public static HashMap<String, Field> createFieldMap(Class classType) {
        HashMap<String, Field> map = new HashMap<>();

        List<Field> fieldList = new ArrayList<>();
        getAllFields(fieldList, classType);

        for (Field field : fieldList) {
            String fieldName = field.getAnnotation(SerializeName.class) != null ? field.getAnnotation(SerializeName.class).value() : field.getName();
            map.put(fieldName, field);
        }
        return map;
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public static void setFieldValue(Object target, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(target, field.getType().cast(value));
    }
}
