package com.mulesoft.jaxrs.raml.jsonschema;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jettison.AbstractXMLStreamWriter;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;
import org.codehaus.jettison.mapped.SimpleConverter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mulesoft.jaxrs.raml.annotation.model.FileUtil;
import com.mulesoft.jaxrs.raml.jaxb.XmlAttribute;
import com.mulesoft.jaxrs.raml.jaxb.XmlObject;


/**
 * <p>JsonUtil class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class JsonUtil {

    /**
     * <p>convertToJSON.</p>
     *
     * @param xmlContent a {@link java.lang.String} object.
     * @param format     a boolean.
     * @return a {@link java.lang.String} object.
     */
    public static String convertToJSON(String xmlContent, boolean format) {
        Document document = null;
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = dBuilder.parse(new InputSource(new StringReader(xmlContent)));
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (document == null)
            return null;
        try {
            Element rootElement = document.getDocumentElement();
            String result = convertToJSON(rootElement);
            if (format) result = formatJSON(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String convertToJSON(Element rootElement) throws Exception {
        Configuration config = new Configuration();
        config.setTypeConverter(new SimpleConverter());
        MappedNamespaceConvention con = new MappedNamespaceConvention(config);
        StringWriter strWriter = new StringWriter();
        AbstractXMLStreamWriter w = new MappedXMLStreamWriter(con, strWriter);
        w.writeStartDocument();
        convertToJSON(rootElement, w);
        w.writeEndDocument();
        w.close();
        strWriter.close();
        String jsonString = strWriter.toString();
        StringWriter wr = new StringWriter();
        StringEscapeUtils.unescapeJavaScript(wr, jsonString);
        String result = wr.toString();
        return result;
    }

    private static void convertToJSON(Element element, AbstractXMLStreamWriter w) throws Exception {
        String elementName = element.getNodeName();
        w.writeStartElement(elementName);
        NamedNodeMap attrs = element.getAttributes();
        int attrsCount = attrs.getLength();
        for (int i = 0; i < attrsCount; i++) {
            Node nd = attrs.item(i);
            if (!(nd instanceof Attr))
                continue;
            Attr attr = (Attr) nd;
            String attrName = attr.getName();
            String attrValue = attr.getValue();
            w.writeAttribute(attrName, attrValue);
        }
        boolean gotChildren = false;
        NodeList childrenList = element.getChildNodes();
        int subNodesCount = childrenList.getLength();
        for (int i = 0; i < subNodesCount; i++) {
            Node nd = childrenList.item(i);
            if (!(nd instanceof Element))
                continue;
            Element el = (Element) nd;
            convertToJSON(el, w);
            gotChildren = true;
        }
        if (gotChildren) {
            w.writeEndElement();
            return;
        }
        String textContent = element.getTextContent();
        w.writeCharacters(textContent);
        w.writeEndElement();
    }

    private static StringBuffer tab(int times) {
        StringBuffer tab = new StringBuffer();
        for (int i = 0; i < times; i++) {
            tab.append('\t');
        }
        return tab;
    }

    public static boolean sameAsJson(Object obj1, Object obj2) throws JSONException {
        if (obj1 == null || obj2 == null) {
            return obj1 == obj2;
        }
        if (obj1.getClass() != obj2.getClass()) {
            return obj1.toString().equals(obj2.toString());
        }
        if (obj1 instanceof JSONObject) {
            JSONObject json1 = (JSONObject) obj1;
            JSONObject json2 = (JSONObject) obj2;
            if (json1.length() != json2.length()) {
                return false;
            }
            Iterator<?> it1 = json1.keys();
            while (it1.hasNext()) {
                String field = it1.next().toString();
                if (!sameAsJson(json1.get(field), json2.get(field))) {
                    return false;
                }
            }
            return true;
        }
        if (obj1 instanceof JSONArray) {
            JSONArray array1 = (JSONArray) obj1;
            JSONArray array2 = (JSONArray) obj2;
            if (array1.length() != array2.length()) {
                return false;
            }
            for (int i = 0; i < array1.length(); i++) {
                if (!sameAsJson(array1.get(i), array2.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return obj1.equals(obj2);
    }

    public static String formatJSON(final String str) {
        StringBuffer sb = new StringBuffer();
        int level = 0;
        boolean textOpen = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(c == '"' && str.charAt(i-1) != '\\') {
                textOpen = !textOpen;
            }
            if(textOpen) {
                sb.append(c);
                continue;
            }
            if (c == '{' || c == '[') {
                sb.append(c).append('\n');
                level++;
                sb.append(tab(level));
            } else if (c == '}' || c == ']') {
                sb.append('\n').append(tab(level - 1)).append(c);
                level--;
            } else if (c == '\n' || c == '\t' || c == '\f' || c == '\r' || c == ' ' || c == 0) {
                continue;
            } else if (c == ',') {
                sb.append(c).append('\n').append(tab(level));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static String convertToJSON(String xmlContent, boolean format, boolean includeRootTag) throws JSONException {
        String json = convertToJSON(xmlContent, false);
        if (!includeRootTag) {
            JSONObject obj = new JSONObject(json);
            if (obj.length() == 1) {
                json = obj.getString(obj.keys().next().toString());
            }
        }
        if (format) {
            return formatJSON(json);
        }
        return json;
    }

    public static String convertJsonToXML(File file, String tagName) throws JSONException {
        return convertJsonToXML(FileUtil.fileToString(file), tagName).toString();
    }

    public static String convertJsonToXML(String json, String tagName) throws JSONException {
        return toXml(new JSONObject(json), tagName).toString();
    }

    public static XmlObject toXml(Object input, String tagName) {
        XmlObject xml = new XmlObject(tagName);
        if (input instanceof JSONObject) {
            JSONObject json = (JSONObject) input;
            Iterator<?> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next().toString();
                Object value = json.opt(key);
                if ("".equals(value)) {
                    xml.addContent(new XmlObject(key));
                } else if (key.startsWith("@")) {
                    String attr = key.substring(key.indexOf("@") + 1);
                    xml.addAttribute(new XmlAttribute(attr, value));
                } else if (value instanceof JSONArray) {
                    JSONArray array = (JSONArray) value;
                    for (int i = 0; i < array.length(); i++) {
                        xml.addContent(toXml(array.opt(i), key));
                    }
                } else {
                    xml.addContent(toXml(value, key));
                }
            }
            return xml;
        }
        if (input != null) {
            if (input.getClass().isArray()) {
                Collection<?> array = Arrays.asList(input);
                input = new JSONArray(array);
            }
            if (input instanceof JSONArray) {
                JSONArray array = (JSONArray) input;
                for (int i = 0; i < array.length(); i++) {
                    xml.addContent(toXml(array.opt(i), tagName == null ? "array" : tagName));
                }
                return xml;
            }
        }

        String text = (input == null) ? "null" : StringEscapeUtils.escapeXml(input.toString());
        if (tagName == null) {
            return xml.addValue(text);
        }
        return new XmlObject(tagName, text);
    }
}
