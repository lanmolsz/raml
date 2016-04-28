package org.raml.jaxrs.codegen.test.jaxb;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
public class Root {

    private Map<String, String> mapProperty;

    public Root() {
        mapProperty = new HashMap<String, String>();
    }

    public Map<String, String> getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map<String, String> map) {
        this.mapProperty = map;
    }

}