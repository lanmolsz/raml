package org.raml.schema.model.serializer;

import com.mulesoft.jaxrs.raml.annotation.model.IMethodModel;
import com.mulesoft.jaxrs.raml.jsonschema.JsonFormatter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.raml.schema.model.ISchemaType;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by E355 on 2016/4/25.
 */
public abstract class SchemaTypeSerializer implements IModelSerializer{
    public String unescapeAndFormat(Object node) {
        if(node instanceof Document) {
            try {
                Document doc = (Document) node;
                TransformerFactory newInstance = TransformerFactory.newInstance();
                newInstance.setAttribute("indent-number", 4);
                Transformer newTransformer = newInstance.newTransformer();
                newTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

                StringWriter writer = new StringWriter();
                newTransformer.transform(new DOMSource(doc), new StreamResult(writer));
                writer.close();
                return writer.toString();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return JsonFormatter.format(StringEscapeUtils.unescapeJavaScript(node.toString()));
    }
}
