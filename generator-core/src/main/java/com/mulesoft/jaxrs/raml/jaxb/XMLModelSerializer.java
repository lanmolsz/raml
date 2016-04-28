package com.mulesoft.jaxrs.raml.jaxb;

import org.raml.schema.model.*;
import org.raml.schema.model.serializer.SchemaTypeSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;

public class XMLModelSerializer extends SchemaTypeSerializer {

    private Document document = null;

    @Override
    public String serialize(ISchemaType type) {
        try {
            this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            document.appendChild(transformToXML(type,type.getName()));
            return unescapeAndFormat(document);
        } catch(ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public Element transformToXML(ISchemaType type,String nodeName) throws ParserConfigurationException{
        Element element = document.createElement(nodeName);

        if(type.isSimple() || type instanceof IEnumSchemaType) {
            element.setTextContent(DefaultValueFactory.getDefaultValue(type).toString());
        }

        if(type instanceof IMapSchemaType) {
            IMapSchemaType map = (IMapSchemaType) type;
            Element entry = document.createElement("entry");
            Element key = document.createElement("key");
            key.setTextContent("str1234");
            entry.appendChild(key);
            entry.appendChild(transformToXML(map.getValueType(),"value"));
            element.appendChild(entry);
        } else if(type instanceof ICollectionSchemaType) {
            ICollectionSchemaType col = (ICollectionSchemaType) type;
            nodeName = col.getElementType().getName();
            if(col.getElementType().isSimple()) {
                nodeName = "value";
            }
            element.appendChild(transformToXML(col.getElementType(),nodeName));
            element.appendChild(transformToXML(col.getElementType(),nodeName));
        }
        List<ISchemaProperty> properties = type.getProperties();
        if(properties != null) {
            for (ISchemaProperty property: properties) {
                String propName = type.getQualifiedPropertyName(property);
                if(property.isAttribute() && property.getType().isSimple()){
                    element.setAttribute(propName,DefaultValueFactory.getDefaultValue(property).toString());
                }else {
                    element.appendChild(transformToXML(property,propName));
                }
            }
        }
        return element;
    }
    public Element transformToXML(ISchemaProperty prop,String nodeName) throws ParserConfigurationException{
        Element element = document.createElement(nodeName);
        if(prop.getType().isSimple()) {
            element.setTextContent(DefaultValueFactory.getDefaultValue(prop).toString());
            return element;
        }
        return transformToXML(prop.getType(),nodeName);
    }

}
