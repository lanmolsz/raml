package com.mulesoft.jaxrs.raml.jaxb;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.mulesoft.jaxrs.raml.annotation.model.ICollectionTypeModel;
import com.mulesoft.jaxrs.raml.annotation.model.IMapTypeModel;
import com.mulesoft.jaxrs.raml.annotation.model.IMethodModel;
import org.apache.commons.lang.StringUtils;
import org.raml.schema.model.*;
import org.raml.schema.model.serializer.IModelSerializer;
import org.raml.schema.model.serializer.SchemaTypeSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mulesoft.jaxrs.raml.annotation.model.StructureType;
import org.w3c.dom.Node;

public class XSDModelSerializer extends SchemaTypeSerializer {
    private Document document = null;



    @Override
    public String serialize(ISchemaType type) {
        try {
            this.document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = document.createElement("xs:schema");
            rootElement.setAttribute("version", "1.0");
            rootElement.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
            document.appendChild(rootElement);
            appendType(type, rootElement, new HashSet<String>(),true);
            return unescapeAndFormat(rootElement.getOwnerDocument());
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

    }


    private void appendType(ISchemaType type, Element rootElement, Set<String> processedTypes, boolean isRootNode) {

        if(type instanceof IMapSchemaType && isRootNode) {
            IMapSchemaType map = (IMapSchemaType) type;
            Element node = buildMapNode(map.getValueType());
            node.setAttribute("name",type.getName());
            rootElement.appendChild(node);
            if(map.getValueType().isComplex()) {
                appendType(map.getValueType(),rootElement,processedTypes,false);
            }
            return ;
        }

        if(type instanceof ICollectionSchemaType && isRootNode) {
            ICollectionSchemaType col = (ICollectionSchemaType) type;
            Node sequenceNode = rootElement.appendChild(element("xs:complexType",type.getName())).appendChild(element("xs:sequence"));
            if(col.getElementType().isSimple()) {
                String name = col.getElementType().getName();
                String xsType = col.getElementType().getMapping().getXsType();
                sequenceNode.appendChild(element("xs:element",name,xsType,false,0,"unbounded"));
            } else {
                String name = col.getElementType().getName();
                sequenceNode.appendChild(element("xs:element",name,name,false,0,"unbounded"));
                appendType(col.getElementType(),rootElement,processedTypes,false);
            }
            return ;
        }

        if(type instanceof IEnumSchemaType) {
            IEnumSchemaType type1 = (IEnumSchemaType) type;
            Element restrict  = (Element) rootElement.appendChild(element("xs:simpleType",type.getName()))
                    .appendChild(element("xs:restriction"));
            restrict.setAttribute("base","xs:string");
            for(String name: type1.values()) {
                Element el = element("xs:enumeration");
                el.setAttribute("value",name);
                restrict.appendChild(el);
            }
            return ;
        }

        List<ISchemaProperty> properties = type.getProperties();
        if (properties != null) {
            if(processedTypes.contains(type.getName())) {
                return;
            }
            processedTypes.add(type.getName());
            Node seq = rootElement.appendChild(element("xs:complexType",type.getName()))
                    .appendChild(element("xs:sequence"));
            for (ISchemaProperty prop : properties) {
                ISchemaType propType = prop.getType();
                String typeName = propType.getName();
                String propName = propType.getQualifiedPropertyName(prop);
                String use = prop.isRequired() ? "required" : null;
                Integer minOccurs = prop.isRequired() ? null : 0;
                if (prop.getType().isComplex()) {
                    if(propType instanceof IMapSchemaType) {
                        propType = ((IMapSchemaType)propType).getValueType();
                        seq.appendChild(element("xs:element",propName)).appendChild(buildMapNode(propType));
                    } else if(propType instanceof ICollectionSchemaType) {
                        propType = ((ICollectionSchemaType)propType).getElementType();
                        typeName = propType.isSimple() ? propType.getMapping().getXsType() :propType.getName();
                        seq.appendChild(element("xs:element",propName,typeName,!prop.isRequired(),minOccurs,"unbounded"));
                    } else {
                        seq.appendChild(element("xs:element",propName,typeName,minOccurs,"unbounded"));
                    }
                    if(prop.getType().isComplex()) {
                        appendType(propType,rootElement,processedTypes,false);
                    }
                } else {
                    if(prop.isAttribute()) {
                        seq.appendChild(element("xs:attribute",propName,propType.getMapping().getXsType(),use));
                    } else {
                        seq.appendChild(element("xs:element",propName,propType.getMapping().getXsType(),minOccurs));
                    }
                }
            }
        }

    }
    private Element buildMapNode(ISchemaType valueType){
        Element root = element("xs:complexType");
        Node innerSequenceNode = root
                .appendChild(element("xs:sequence"))
                .appendChild(element("xs:element","entry",0,"unbounded"))
                .appendChild(element("xs:complexType"))
                .appendChild(element("xs:sequence"));

        innerSequenceNode.appendChild(element("xs:element","key","xs:string",0));

        String xsType = valueType.getName();
        if(valueType.isSimple()) {
            xsType = valueType.getMapping().getXsType();
        }
        innerSequenceNode.appendChild(element("xs:element","value",xsType,0));
        return root;
    }

    private Element element(String tagName){
        return element(tagName,null);
    }
    private Element element(String tagName,String name){
        return element(tagName,name,null,null,null);
    }
    private Element element(String tagName,String name, String type, Integer minOccurs){
        return element(tagName,name,type,minOccurs,null);
    }

    private Element element(String tagName,String name, Integer minOccurs, String maxOccurs){
        return element(tagName,name,null,minOccurs,maxOccurs);
    }

    private Element element(String tagName,String name, String type, Integer minOccurs, String maxOccurs){
        return element(tagName,name,type,null,minOccurs,maxOccurs);
    }
    private Element element(String tagName,String name, String type, Boolean nullable,Integer minOccurs, String maxOccurs){
        if(StringUtils.isBlank(tagName)) {
            throw new IllegalArgumentException("Parameter tagName can't be blank");
        }
        Element element = document.createElement(tagName);

        if(StringUtils.isNotEmpty(name)){
            element.setAttribute("name",name);
        }
        if(StringUtils.isNotEmpty(type)){
            element.setAttribute("type",type);
        }
        if(nullable != null) {
            element.setAttribute("nillable",String.valueOf(nullable));
        }
        if(minOccurs != null){
            element.setAttribute("minOccurs",String.valueOf(minOccurs));
        }
        if(StringUtils.isNotEmpty(maxOccurs)){
            element.setAttribute("maxOccurs",maxOccurs);
        }

        return element;
    }
    private Element element(String tagName,String name, String type,String use){
        if(StringUtils.isBlank(tagName)) {
            throw new IllegalArgumentException("Parameter tagName can't be blank");
        }
        Element element = document.createElement(tagName);

        if(StringUtils.isNotEmpty(name)){
            element.setAttribute("name",name);
        }
        if(StringUtils.isNotEmpty(type)){
            element.setAttribute("type",type);
        }
        if(StringUtils.isNotEmpty(use)) {
            element.setAttribute("use",use);
        }

        return element;
    }
}
