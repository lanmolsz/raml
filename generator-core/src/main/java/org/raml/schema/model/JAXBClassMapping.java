package org.raml.schema.model;

import java.util.HashMap;
import java.util.Map;

public enum JAXBClassMapping {

    BIG_INTEGER(
            "java.math.BigInteger",
            "java.lang.Integer",
            "xs:integer",
            "123456789123456789"),
    BIG_DECIMAL(
            "java.math.BigDecimal",
            "java.lang.Double",
            "xs:decimal",
            "123456789123456789.123456789123456789"),

    BOOLEAN(
            "java.lang.Boolean",
            "boolean",
            "xs:boolean",
            "false"),
    PRIMITIVE_BOOLEAN(
            "boolean",
            "boolean",
            "xs:boolean",
            "false"),
    BYTE(
            "java.lang.Byte",
            "byte",
            "xs:byte",
            "127"),
    PRIMITIVE_BYTE(
            "byte",
            "byte",
            "xs:byte",
            "127"),
    CHAR(
            "char",
            "java.lang.String",
            "xs:string",
            "a"),
    CHARACTER(
            "java.lang.Character",
            "java.lang.String",
            "xs:string",
            "a"),
    SHORT(
            "java.lang.Short",
            "short",
            "xs:short",
            "1234"),
    PRIMITIVE_SHORT(
            "short",
            "short",
            "xs:short",
            "1234"),
    INT(
            "int",
            "int",
            "xs:int",
            "1234"),
    INTEGER(
            "java.lang.Integer",
            "java.lang.Integer",
            "xs:integer",
            "1234"),
    PRIMITIVE_LONG(
            "long",
            "long",
            "xs:long",
            "1234"
    ),
    LONG(
            "java.lang.Long",
            "long",
            "xs:long",
            "1234"
    ),

    PRIMITIVE_FLOAT(
            "float",
            "float",
            "xs:float",
            "1234.0"
    ),
    FLOAT(
            "java.lang.Float",
            "float",
            "xs:float",
            "1234.0"
    ),

    PRIMITIVE_DOUBLE(
            "double",
            "double",
            "xs:double",
            "1234.1234"
    ),
    DOUBLE(
            "java.lang.Double",
            "double",
            "xs:double",
            "1234.1234"
    ),

    STRING(
            "java.lang.String",
            "java.lang.String",
            "xs:string",
            "str1234"
    ),
    CALENDAR(
            "java.util.Calendar",
            "java.lang.String",
            "xs:dateTime",
            "2002-09-24+06:00"),
    DATE(
            "java.util.Date",
            "java.lang.String",
            "xs:dateTime",
            "2002-09-24+06:00"),
    Q_NAME(
            "javax.xml.namespace.QName",
            "java.lang.String",
            "xs:QName",
            "data:someClassReferencedByQName"),
    URI(
            "java.net.URI",
            "java.lang.String",
            "xs:string",
            "http://www.raml.renerator.example.uri.com"),
    XML_GREGORIAN_CALENDAR(
            "javax.xml.datatype.XMLGregorianCalendar",
            "java.lang.String",
            "xs:anySimpleType",
            "1991-01-24'PSL'13:05:33"),
    DURATION(
            "javax.xml.datatype.Duration",
            "java.lang.String",
            "xs:duration",
            "P1Y2M3DT5H20M30.123S"),
    IMAGE(
            "java.awt.Image",
            "java.lang.String",
            "xs:base64Binary",
            "U29tZSBkYXRh"),
    DATA_HANDLER(
            "javax.activation.DataHandler",
            "java.lang.String",
            "xs:base64Binary",
            "U29tZSBkYXRh"),
    SOURCE(
            "javax.xml.transform.Source",
            "java.lang.String",
            "xs:base64Binary",
            "U29tZSBkYXRh"),
    UUID(
            "java.util.UUID",
            "java.lang.String",
            "xs:string",
            "d9515ea0-1638-4de9-8983-339f72e94fc6");


    JAXBClassMapping(String originalClass, String mappingClass, String xsType, String example) {
        this.originalClass = originalClass;
        this.mappingClass = mappingClass;
        this.xsType = xsType;
        this.example = example;
    }

    private final String originalClass;
    private final String mappingClass;
    private final String xsType;
    private final String example;

    private static Map<String, JAXBClassMapping> mapping = new HashMap<String, JAXBClassMapping>();

    static {
        for (JAXBClassMapping mapping : JAXBClassMapping.values()) {
            JAXBClassMapping.mapping.put(mapping.getOriginalClass(), mapping);
        }
    }


    public static JAXBClassMapping getMapping(String str) {
        return mapping.get(str);
    }

    public String getOriginalClass() {
        return originalClass;
    }

    public String getMappingClass() {
        return mappingClass;
    }

    public String getXsType() {
        return xsType;
    }

    public String getExample() {
        return example;
    }


}
