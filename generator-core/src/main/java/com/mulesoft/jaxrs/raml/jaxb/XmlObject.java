package com.mulesoft.jaxrs.raml.jaxb;

import java.util.ArrayList;
import java.util.List;


public class XmlObject {
    private String tagName;
    private StringBuffer value;
    private List<XmlAttribute> attributes = new ArrayList<XmlAttribute>();
    private List<XmlObject> content = new ArrayList<XmlObject>();
    private XmlObject parent;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public StringBuffer getValue() {
        return value;
    }

    public XmlObject addValue(String value) {
        if (value == null) return this;
        if (this.value == null) {
            this.value = new StringBuffer(value);
        } else {
            this.value.append("\n").append(value);
        }
        return this;
    }

    public List<XmlAttribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(XmlAttribute attr) {
        this.attributes.add(attr);
    }

    public List<XmlObject> getContent() {
        return content;
    }

    public void addContent(XmlObject o) {
        content.add(o);
        o.parent = this;
    }

    public XmlObject() {
        this(null);
    }

    public XmlObject(String tag) {
        this(tag, null);
    }

    public XmlObject(String tag, String value) {
        this.tagName = tag;
        if (value != null) {
            this.value = new StringBuffer(value);
        }
    }

    private int getLevel() {
        int indent = 0;
        XmlObject parent = this.parent;
        while (parent != null) {
            indent++;
            parent = parent.parent;
        }
        return indent;
    }

    private StringBuffer indent() {
        int level = getLevel();
        StringBuffer indent = new StringBuffer();
        for (int i = 0; i < level; i++) {
            indent.append("\t");
        }
        return indent;
    }

    @Override
    public String toString() {
        if (tagName == null || tagName.trim().length() == 0) {
            return "'" + value + "'";
        }
        StringBuffer xml = new StringBuffer(indent());
        xml.append("<").append(tagName);
        for (XmlAttribute xmlAttribute : attributes) {
            xml.append(" ").append(xmlAttribute.toString());
        }
        if (value != null && value.length() > 0) {
            return xml.append(">").append(value).append("</" + tagName + ">\n").toString();
        }
        if (content.size() == 0) {
            return xml.append("/>").toString();
        } else {
            xml.append(">\n");
        }
        for (XmlObject o : content) {
            xml.append(o.toString());
        }
        return xml.append(indent()).append("</" + tagName + ">\n").toString();
    }

}
