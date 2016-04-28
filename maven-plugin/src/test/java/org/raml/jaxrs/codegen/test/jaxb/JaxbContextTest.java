package org.raml.jaxrs.codegen.test.jaxb;


import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.logging.Logger;

public class JaxbContextTest {
    private Logger logger = Logger.getLogger("JaxbContextTest");
    @Test
    public void testJaxbFirst() throws JAXBException, IOException {
        JAXBContext ctx = JAXBContext.newInstance(Person.class);
        ctx.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                logger.warning("namespaceUri:" + namespaceUri);
                logger.warning("suggestedFileName:" + suggestedFileName);
                File file = new File("E:/person.xsd");
                StreamResult result = new StreamResult(file);
                result.setSystemId(file.toURI().toURL().toString());
                return result;
            }
        });
    }


    @Test
    public void testJaxbSecond() throws JAXBException, IOException {
        JAXBContext ctx = JAXBContext.newInstance(Person[].class);
        ctx.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                logger.warning("namespaceUri:" + namespaceUri);
                logger.warning("suggestedFileName:" + suggestedFileName);
                File file = new File("E:/arrays.xsd");
                StreamResult result = new StreamResult(file);
                result.setSystemId(file.toURI().toURL().toString());
                return result;
            }
        });
    }

    @Test
    public void testJaxbThird() throws JAXBException, IOException {
        Collection<Person> collection = new ArrayList<Person>();

        JAXBContext ctx = JAXBContext.newInstance(collection.getClass());
        ctx.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                logger.warning("namespaceUri:" + namespaceUri);
                logger.warning("suggestedFileName:" + suggestedFileName);
                File file = new File("E:/collection.xsd");
                StreamResult result = new StreamResult(file);
                result.setSystemId(file.toURI().toURL().toString());
                return result;
            }
        });
    }

    @Test
    public void testJaxbFourth() throws JAXBException, IOException {
        Map<String,Person> map = new HashMap<String, Person>();

        JAXBContext ctx = JAXBContext.newInstance(map.getClass());
        ctx.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                logger.warning("namespaceUri:" + namespaceUri);
                logger.warning("suggestedFileName:" + suggestedFileName);
                File file = new File("E:/map.xsd");
                StreamResult result = new StreamResult(file);
                result.setSystemId(file.toURI().toURL().toString());
                return result;
            }
        });
    }


    @Test
    public void testJaxbFifth() throws JAXBException, IOException, NoSuchMethodException {
        Method method = Person.class.getMethod("getExperiences");
        JAXBContext ctx = JAXBContext.newInstance(method.getReturnType());
        ctx.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                logger.warning("namespaceUri:" + namespaceUri);
                logger.warning("suggestedFileName:" + suggestedFileName);
                File file = new File("E:/returnType.xsd");
                StreamResult result = new StreamResult(file);
                result.setSystemId(file.toURI().toURL().toString());
                return result;
            }
        });
    }

}
