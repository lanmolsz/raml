package com.mulesoft.jaxrs.raml.jsonschema;

import com.mulesoft.jaxrs.raml.annotation.model.IAnnotationModel;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.raml.schema.model.*;
import org.raml.schema.model.serializer.SchemaTypeSerializer;

import java.util.HashMap;
import java.util.List;

/**
 * Created by E355 on 2016/4/25.
 */
public class JsonSchemaSerializer extends SchemaTypeSerializer{

    private static final String ITEMS = "items";

    private static final String PROPERTIES = "properties";

    private static final String PATTERN_PROPERTIES = "patternProperties";

    private static final String DEFAULT_REGEXP = "[a-zA-Z0-9]+";

    private static final HashMap<String, String> typeMap = new HashMap<String, String>();

    static {
        typeMap.put(SimpleType.INTEGER.getClassName(), "number");
        typeMap.put(SimpleType.LONG.getClassName(), "number");
        typeMap.put(SimpleType.SHORT.getClassName(), "number");
        typeMap.put(SimpleType.BYTE.getClassName(), "number");
        typeMap.put(SimpleType.DOUBLE.getClassName(), "number");
        typeMap.put(SimpleType.FLOAT.getClassName(), "number");
        typeMap.put(SimpleType.BOOLEAN.getClassName(), "boolean");
        typeMap.put(SimpleType.CHARACTER.getClassName(), "string");
        typeMap.put(SimpleType.STRING.getClassName(), "string");
    }


    public String serialize(ISchemaType type){
        try {
            return buildRootJSONSchema(type);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String buildRootJSONSchema(ISchemaType type) throws JSONException {
        JSONObject root = transformToJSON(type,true,true,type.getAnnotations());
        return super.unescapeAndFormat(root);
    }

    public JSONObject transformToJSON(ISchemaType type,boolean isRootNode,boolean required, List<IAnnotationModel> annotations) throws JSONException {
        JSONObject node = new JSONObject();
        if(isRootNode) {
            node.put("$schema", "http://json-schema.org/draft-03/schema");
        }
        node.put("required", required);
        node.put("type", getJSONMappingType(type));
        if(annotations != null) {
            for (IAnnotationModel annotation : annotations) {
                //TODO Add field check
            }
        }

        if(type instanceof IMapSchemaType) {
            JSONObject child = new JSONObject();
            IMapSchemaType mapType = (IMapSchemaType)type;
            ISchemaType keyType = mapType.getKeyType();
            ISchemaType valueType = mapType.getValueType();
            if (!keyType.getClassName().equals(SimpleType.STRING.getClassName())) {
                throw new IllegalArgumentException("Invalid map key type. Only String is available as key type.");
            }
            annotations = valueType.getAnnotations();
            child.put(DEFAULT_REGEXP, transformToJSON(valueType,false,false,annotations));
            node.put(PATTERN_PROPERTIES, child);
        } else if(type instanceof ICollectionSchemaType) {
            ISchemaType elementType = ((ICollectionSchemaType)type).getElementType();
            JSONArray items = new JSONArray();
            annotations = elementType.getAnnotations();
            items.put(transformToJSON(elementType,false,false,annotations));
            node.put(ITEMS,items);
        } else if(type.isComplex()){
            List<ISchemaProperty> properties = type.getProperties();
            if(properties != null) {
                for (ISchemaProperty property: properties) {
                    try {
                        annotations = property.getAnnotations();
                        String propName = type.getQualifiedPropertyName(property);
                        if(property.isAttribute() && property.getType().isSimple()) {
                            propName = "@" + propName;
                        }
                        node.put(propName,transformToJSON(property.getType(),false,property.isRequired(),annotations));
                    } catch (IllegalArgumentException e) {
                        String errorMessage = e.getMessage();
                        errorMessage += " Type: " + type.getClassQualifiedName() +
                                " Property: " + property.getName();
                        throw new IllegalArgumentException(errorMessage);
                    }
                }
            }
        }
        return node;
    }

    private String getJSONMappingType(ISchemaType type) {
        if (type.isSimple()) {
            return typeMap.get(type.getClassName());
        }
        return type instanceof ICollectionSchemaType ? "array" : "object";
    }


}
