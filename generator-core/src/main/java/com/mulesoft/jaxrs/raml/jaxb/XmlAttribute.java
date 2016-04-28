package com.mulesoft.jaxrs.raml.jaxb;

import org.apache.commons.lang.math.NumberUtils;


public class XmlAttribute {
    private String name;
    private String value;
    private XmlAttributeType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public XmlAttributeType getType() {
        return type;
    }

    public void setType(XmlAttributeType type) {
        this.type = type;
    }

    public XmlAttribute(String name, Object value) {
        this(name, value, null);
    }

    public XmlAttribute(String name, Object value, XmlAttributeType type) {
        super();
        this.name = name;
        this.value = value == null ? "" : value.toString();
        this.type = type;
        if (this.type == null) {
            if (NumberUtils.isNumber(this.value)) {
                this.type = XmlAttributeType.NUMBER;
            } else if ("true".equalsIgnoreCase(this.value) || "false".equals(this.value)) {
                this.type = XmlAttributeType.BOOLEAN;
            } else {
                this.type = XmlAttributeType.STRING;
            }
        }
    }

    @Override
    public String toString() {
        if (this.type == XmlAttributeType.STRING) {
            value = "'" + value + "'";
        }
        return name + "=" + value;
    }

}
