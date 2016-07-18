package org.raml.jaxrs.codegen.model;

import com.mulesoft.jaxrs.raml.annotation.model.*;
import com.mulesoft.jaxrs.raml.annotation.model.reflection.RuntimeResourceVisitor;
import com.mulesoft.jaxrs.raml.jaxb.JAXBRegistry;
import com.mulesoft.jaxrs.raml.jaxb.SchemaModelBuilder;
import com.mulesoft.jaxrs.raml.jaxb.XMLModelSerializer;
import com.mulesoft.jaxrs.raml.jaxb.XSDModelSerializer;
import com.mulesoft.jaxrs.raml.jsonschema.JsonModelSerializer;
import com.mulesoft.jaxrs.raml.jsonschema.JsonSchemaSerializer;
import com.mulesoft.jaxrs.raml.jsonschema.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.raml.jaxrs.codegen.util.MethodModelUtils;
import org.raml.schema.model.ISchemaType;
import org.raml.schema.model.serializer.SchemaTypeSerializer;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MavenResourceVisitor extends ResourceVisitor {
	protected JAXBRegistry registry = new JAXBRegistry();
	protected SchemaModelBuilder builder = new SchemaModelBuilder(registry,config);

	public MavenResourceVisitor(File outputFile, ClassLoader classLoader,IRamlConfig config) {
		super(outputFile, classLoader);
		setPreferences(config);
	}

	public void visitAll(Collection<ITypeModel> models){
		for (ITypeModel type : models) {
			visit(type);
		}
	}

	protected IMethodModel[] extractMethods(ITypeModel model) {
		return MethodModelUtils.getRestMethods(model);
	}

	@Override
	protected boolean generateXMLSchema(ITypeModel model) {
		Class<?> element = model.getActualClass();
		if(element != null && element != Void.TYPE){
			ISchemaType schemaModel = builder.buildSchemaModel(registry.getJAXBModel(model));
			generateSchema(new JsonSchemaSerializer(),model,schemaModel);
			generateSchema(new XSDModelSerializer(),model,schemaModel);
			generateExample(model ,element ,schemaModel);
			return true;
		}
		return false;
	}


	private void generateSchema(SchemaTypeSerializer serializer,ITypeModel model,ISchemaType schemaModel){
		try {
			boolean isJSON = serializer instanceof JsonSchemaSerializer;
			File schemaFile = getFile(model,SCHEMA,isJSON ? JSON:XML);
			String content = serializer.serialize(schemaModel);
			writeToFile(content,schemaFile);
			spec.getCoreRaml().addGlobalSchema(schemaFile.getName(), content);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private File getFile(ITypeModel model, String fileType, String mediaType){
		String fileName = getSimpleQualifiedName(model.getGenericName());
		return constructFileLocation(fileName,fileType,mediaType,null);
	}

	private void generateExample(ITypeModel model, Class<?> element,ISchemaType schemaModel){
		File jsonFile = getFile(model,EXAMPLE,JSON);
		File xmlFile =  getFile(model,EXAMPLE,XML);
		try{
			String[] contents = getExampleContent(element,model.getName(), model.getDocumentation());
			if(StringUtils.isNotEmpty(contents[0])) {
				writeToFile(contents[0], jsonFile);
			}
			if(StringUtils.isNotEmpty(contents[1])) {
				writeToFile(contents[1], xmlFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		generateDefaultExampleIfNotExists(schemaModel,jsonFile, xmlFile);
	}

	protected String[] getExampleContent(Class<?> clazz,String root, String documentation) throws JSONException,ClassNotFoundException {
		if(StringUtils.isNotBlank(documentation)) {
			Iterator<String> it = Arrays.asList(documentation.split("\n")).iterator();
			Pattern start = Pattern.compile("[ \t]?@example[ \t]+");
			while(it.hasNext()) {
				String line = it.next();
				if(StringUtils.isNotBlank(line)) {
					Matcher startMatcher = start.matcher(line);
					String content = "";
					if(line.endsWith("@example")) {
						content = readExampleContent(it, clazz, "").trim();
					}
					if(startMatcher.find()) {
						content = readExampleContent(it, clazz, line.substring(startMatcher.end()).trim()).trim();
					}
					if(StringUtils.isNotEmpty(content)) {
						if (content.startsWith("<")) {
							return new String[]{ JsonUtil.convertToJSON(content, true, false), content};
						} else if (content.startsWith("{") || content.startsWith("[")) {
							return new String[]{JsonUtil.formatJSON(content), JsonUtil.convertJsonToXML(content, root)};
						}
					}
				}
			}
		}
		return new String[]{"",""};
	}

	protected String readExampleContent(Iterator<String> it, Class<?> clazz, String line){
		Pattern end = Pattern.compile("[ \t]?@[a-zA-Z]+[ \t]+");
		String exampleCode = "";
		boolean exampleContent = false;
		while(it.hasNext()) {
			Matcher endMatcher = end.matcher(line);
			if(endMatcher.find()) {
				return exampleCode;
			}
			if (isExamplePath(line)) {
				if (line.startsWith("classpath:")) {
					line = clazz.getResource(line.replace("classpath:", "")).getFile();
				} else if (line.startsWith("file:")) {
					line = line.replace("file:", "");
				}
				File file = new File(line);
				if (file.exists() && file.isFile()) {
					exampleCode = FileUtil.fileToString(file);
					return exampleCode != null ? exampleCode.trim() : "";
				}
				break;
			} else if (exampleContent || isExampleContent(line)) {
				exampleCode = exampleCode + line +"\n";
				exampleContent = true;
			}
			line = it.next().replaceFirst("(\\s*)\\*(\\s*)","").trim();
		}
		return exampleCode;
	}

	private void generateDefaultExampleIfNotExists(ISchemaType schemaModel, File jsonFile,File xmlFile){
		if(jsonFile != null && !jsonFile.exists()) {
			writeToFile(new JsonModelSerializer().serialize(schemaModel), jsonFile);
		}
		if(xmlFile != null && !xmlFile.exists()) {
			writeToFile(new XMLModelSerializer().serialize(schemaModel), xmlFile);
		}
	}

	protected ResourceVisitor createResourceVisitor() {
		return new MavenResourceVisitor(outputFile, classLoader,config);
	}


}
