package com.icehousecorp.jsonapi;

import com.github.fge.uritemplate.URITemplate;
import com.github.fge.uritemplate.URITemplateException;
import com.github.fge.uritemplate.URITemplateParseException;
import com.github.fge.uritemplate.vars.VariableMap;
import com.github.fge.uritemplate.vars.VariableMapBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by imrenagi on 2/16/15.
 */
public class JSONAPIResourceObject implements Serializable {

    public String id;
    public String type;
    public String href;

    public void setType(String type) {
        if (this.type == null) {
            this.type = type;
        }
    }

    public void setHref(String href, String linksKey) {
        if (this.href == null) {
            HashMap<String, Object> map = createParameterMap(linksKey, this.id);
            this.href = createUrl(href, createVariableMap(map));
        }
    }

    private HashMap<String, Object> createParameterMap(String key, String idValue) {
        if (key != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(key, idValue);
            return map;
        } else {
            return null;
        }
    }

    /**
     * * Build a VariableMap instance
     *
     * @param map key-value pairs for parameter in URI Template
     * @return VariableMap
     */
    private VariableMap createVariableMap(HashMap<String, Object> map) {
        final VariableMapBuilder builder = VariableMap.newBuilder();

        Iterator<String> iterator = map.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (map.get(key) != null) {
                builder.addScalarValue(key, map.get(key));
            }
        }

        final VariableMap vars = builder.freeze();
        return vars;
    }

    /**
     * * Generate URI String with parameter included in vars
     * *
     *
     * @param endpointPath an URI Template
     * @param vars
     * @return combined endpoint.
     */
    private String createUrl(String endpointPath, VariableMap vars) {
        String uri = null;
        try {
            if (endpointPath != null) {
                URITemplate uriTemplate = new URITemplate(endpointPath);
                uri = uriTemplate.toString(vars);
            }
        } catch (URITemplateParseException e) {
            e.printStackTrace();
        } catch (URITemplateException e) {
            e.printStackTrace();
        }
        return uri;
    }
}
