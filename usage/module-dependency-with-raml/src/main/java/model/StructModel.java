package model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by E355 on 2016/4/26.
 */
public class StructModel {
    private Map<Gender, RamlModel> ramlMap;
    private List<Gender> list;
    private Collection<RamlModel> collection;
    private Set<RamlModel> set;
    private Gender[] array;

    public Map<Gender, RamlModel> getRamlMap() {
        return ramlMap;
    }

    public void setRamlMap(Map<Gender, RamlModel> ramlMap) {
        this.ramlMap = ramlMap;
    }

    public List<Gender> getList() {
        return list;
    }

    public void setList(List<Gender> list) {
        this.list = list;
    }

    public Collection<RamlModel> getCollection() {
        return collection;
    }

    public void setCollection(Collection<RamlModel> collection) {
        this.collection = collection;
    }

    public Set<RamlModel> getSet() {
        return set;
    }

    public void setSet(Set<RamlModel> set) {
        this.set = set;
    }

    public Gender[] getArray() {
        return array;
    }

    public void setArray(Gender[] array) {
        this.array = array;
    }
}
