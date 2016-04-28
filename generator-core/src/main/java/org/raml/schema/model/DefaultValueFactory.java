package org.raml.schema.model;

import java.util.HashMap;

public class DefaultValueFactory {

    public static final int DEFAULT_INTEGER_VALUE = 123;

    public static final double DEFAULT_DOUBLE_VALUE = 123.456;

    public static final float DEFAULT_FLOAT_VALUE = 123.456F;

    public static final char DEFAULT_CHARACTER_VALUE = 'a';

    public static final String DEFAULT_STRING_VALUE = "str1234";

    public static final boolean DEFAULT_BOOLEAN_VALUE = true;

    private static HashMap<SimpleType, Object> valueMap = new HashMap<SimpleType, Object>();

    static {
        valueMap.put(SimpleType.INTEGER, DefaultValueFactory.DEFAULT_INTEGER_VALUE);
        valueMap.put(SimpleType.LONG, DefaultValueFactory.DEFAULT_INTEGER_VALUE);
        valueMap.put(SimpleType.SHORT, DefaultValueFactory.DEFAULT_INTEGER_VALUE);
        valueMap.put(SimpleType.BYTE, DefaultValueFactory.DEFAULT_INTEGER_VALUE);
        valueMap.put(SimpleType.DOUBLE, DefaultValueFactory.DEFAULT_DOUBLE_VALUE);
        valueMap.put(SimpleType.FLOAT, DefaultValueFactory.DEFAULT_FLOAT_VALUE);
        valueMap.put(SimpleType.BOOLEAN, DefaultValueFactory.DEFAULT_BOOLEAN_VALUE);
        valueMap.put(SimpleType.CHARACTER, DefaultValueFactory.DEFAULT_CHARACTER_VALUE);
        valueMap.put(SimpleType.STRING, DefaultValueFactory.DEFAULT_STRING_VALUE);
    }

    public static Object getDefaultValue(ISchemaProperty prop) {

        String propValue = prop.getDefaultValue();
        if (propValue != null) {
            return propValue;
        }
        return getDefaultValue(prop.getType());
    }

    public static Object getDefaultValue(ISchemaType type) {

        JAXBClassMapping mapping = type.getMapping();
        if (mapping != null) {
            return mapping.getExample();
        }

        if (type.isSimple()) {
            if (type instanceof SimpleType) {
                return valueMap.get((SimpleType) type);
            }
        }
        if(type instanceof IEnumSchemaType) {
            for(String name : ((IEnumSchemaType)type).values()) {
                return name;
            }
        }
        return "some " + type.getName() + " value";
    }

}
