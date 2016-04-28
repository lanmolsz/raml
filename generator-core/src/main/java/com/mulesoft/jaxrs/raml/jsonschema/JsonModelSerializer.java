package com.mulesoft.jaxrs.raml.jsonschema;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.raml.schema.model.*;
import org.raml.schema.model.serializer.SchemaTypeSerializer;

import java.util.List;

public class JsonModelSerializer extends SchemaTypeSerializer {

    @Override
    public String serialize(ISchemaType type) {
        try{
            return unescapeAndFormat(transformToJSON(type));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Object transformToJSON(ISchemaType type) throws JSONException {
        if(type.isSimple() || type instanceof IEnumSchemaType) {
            return DefaultValueFactory.getDefaultValue(type);
        }
        JSONObject node = new JSONObject();
        if(type instanceof IMapSchemaType) {
            IMapSchemaType map = (IMapSchemaType) type;
            node.put("key_1",transformToJSON(map.getValueType()));
            node.put("key_2",transformToJSON(map.getValueType()));
        } else if(type instanceof ICollectionSchemaType) {
            ICollectionSchemaType col = (ICollectionSchemaType) type;
            JSONArray items = new JSONArray();
            items.put(transformToJSON(col.getElementType()));
            items.put(transformToJSON(col.getElementType()));
            return items;
        }
        List<ISchemaProperty> properties = type.getProperties();
        if(properties != null) {
            for (ISchemaProperty property: properties) {
                String propName = type.getQualifiedPropertyName(property);
                if(property.isAttribute() && property.getType().isSimple()){
                    propName = "@"+propName;
                }
                if(property.getType().isSimple()) {
                    node.put(propName,DefaultValueFactory.getDefaultValue(property));
                } else {
                    node.put(propName,transformToJSON(property.getType()));
                }
            }
        }
        return node;
    }
}
