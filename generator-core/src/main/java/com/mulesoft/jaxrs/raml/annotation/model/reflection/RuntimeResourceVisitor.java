
package com.mulesoft.jaxrs.raml.annotation.model.reflection;

import com.mulesoft.jaxrs.raml.annotation.model.IRamlConfig;
import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import com.mulesoft.jaxrs.raml.annotation.model.ResourceVisitor;
import com.mulesoft.jaxrs.raml.annotation.model.StructureType;
import com.mulesoft.jaxrs.raml.jsonschema.JsonFormatter;
import com.mulesoft.jaxrs.raml.jsonschema.JsonUtil;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.raml.parser.tagresolver.TagResolver;

import java.io.File;

/**
 * <p>RuntimeResourceVisitor class.</p>
 *
 * @author kor
 * @version $Id: $Id
 */
public class RuntimeResourceVisitor extends ResourceVisitor {


	/**
	 * <p>Constructor for RuntimeResourceVisitor.</p>
	 *
	 * @param outputFile a {@link java.io.File} object.
	 * @param classLoader a {@link java.lang.ClassLoader} object.
	 */
	public RuntimeResourceVisitor(File outputFile, ClassLoader classLoader) {
		super(outputFile, classLoader);
	}

	/**
	 * <p>Constructor for RuntimeResourceVisitor.</p>
	 *
	 * @param outputFile a {@link java.io.File} object.
	 * @param classLoader a {@link java.lang.ClassLoader} object.
	 * @param preferencesConfig a {@link com.mulesoft.jaxrs.raml.annotation.model.IRamlConfig} object.
	 */
	public RuntimeResourceVisitor(File outputFile, ClassLoader classLoader, IRamlConfig preferencesConfig) {
		super(outputFile, classLoader);
		setPreferences(preferencesConfig);
	}

	/**
	 * <p>getProperJSONExampleFromXML.</p>
	 *
	 * @param generateXMLExampleJAXB a {@link java.lang.String} object.
	 * @param t 
	 * @return a {@link java.lang.String} object.
	 */
	protected String getProperJSONExampleFromXML(String generateXMLExampleJAXB, ITypeModel t) {		
		
		String jsonText = JsonUtil.convertToJSON(generateXMLExampleJAXB, true);
		JSONObject c;
		try {
			c = new JSONObject(jsonText);
			jsonText=c.get((String) c.keys().next()).toString();
		} catch (JSONException e) {
			//should never happen
			throw new IllegalStateException(e);
		}
		jsonText = JsonFormatter.format(jsonText);
		return jsonText;
	}
	
	/** {@inheritDoc} */
	@Override
	protected boolean generateXMLSchema(ITypeModel type) {
		String name = type.getFullyQualifiedName();
		if(name.equals("void")||name.equals("java.lang.Void")){
			return false;
		}
		Class<?> element = null;
		if (type instanceof ReflectionType) {
			element = ((ReflectionType) type).getElement();
		}
		else if (type.getFullyQualifiedName() != null && classLoader != null) {
			try {
				element = classLoader.loadClass(type.getFullyQualifiedName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if(element==null){
			return false;
		}
		StructureType st = getStructureType(type);
		if(st == null || st == StructureType.COMMON){
			generateXSDForClass(element);
		}
		afterSchemaGen(type,st);
		return true;
	}


	
	/**
	 * <p>createResourceVisitor.</p>
	 *
	 * @return a {@link com.mulesoft.jaxrs.raml.annotation.model.ResourceVisitor} object.
	 */
	protected ResourceVisitor createResourceVisitor() {
		return new RuntimeResourceVisitor(outputFile, classLoader);
	}

}
