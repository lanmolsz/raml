package org.raml.jaxrs.codegen.extension;

import com.mulesoft.jaxrs.raml.annotation.model.ITypeModel;
import com.mulesoft.jaxrs.raml.jaxb.ISchemaModelBuilderExtension;
import com.mulesoft.jaxrs.raml.jaxb.JAXBProperty;
import org.raml.jaxrs.codegen.maven.TypeModelRegistry;
import org.raml.schema.model.ISchemaProperty;
import org.raml.schema.model.ISchemaType;
import org.raml.schema.model.impl.PropertyModelImpl;
import org.raml.schema.model.impl.TypeModelImpl;


public class DefaultSchemaModelBuilderExtension implements ISchemaModelBuilderExtension {
    @Override
    public ISchemaProperty processProperty(JAXBProperty jaxbProp, ISchemaProperty prop) {
        return prop;
    }

    @Override
    public void processType(ISchemaType type) {
    }

    @Override
    public void processModel(ISchemaType type) {
    }

    @Override
    public boolean generateSchema(ITypeModel type) {
        return true;
    }

    private static final DefaultSchemaModelBuilderExtension DEFAULT_EXT = new DefaultSchemaModelBuilderExtension();

    public static DefaultSchemaModelBuilderExtension getDefaultExt(){
        return DEFAULT_EXT;
    }
    private DefaultSchemaModelBuilderExtension(){}
}
