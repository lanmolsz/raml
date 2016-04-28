package com.mulesoft.jaxrs.raml.jaxb;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Created by Administrator on 2016/4/22.
 */
public class JAXBAnnotationTest {
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @Before
    public void setup() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(State.class);
        marshaller = context.createMarshaller();
        unmarshaller = context.createUnmarshaller();
    }

    @Test
    public void marshallerTest() throws JAXBException {
        State state = new State();
        state.setId(1);
        state.setName("中国");
        state.setPopulation(1400000000L);
        state.setUri("http://www.china.cn");
        marshaller.marshal(state,System.out);
    }
}
