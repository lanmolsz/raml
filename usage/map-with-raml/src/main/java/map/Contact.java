package map;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by E355 on 2016/4/20.
 */
public class Contact {

    private String name;
    private String mobile;
    private Map<String,String> hobby;
    private Map<String,Location> locationMap;
    private Location[] locationArray;
//    private List<Location> locationList;
//    private Set<Location> locationSet;
//    private Collection<Location> locationCollection;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<String,String> getHobby() {
        return hobby;
    }

    public void setHobby(Map<String,String> hobby) {
        this.hobby = hobby;
    }

    public Map<String, Location> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(Map<String, Location> locationMap) {
        this.locationMap = locationMap;
    }

    public Location[] getLocationArray() {
        return locationArray;
    }

    public void setLocationArray(Location[] locationArray) {
        this.locationArray = locationArray;
    }

//    public List<Location> getLocationList() {
//        return locationList;
//    }
//
//    public void setLocationList(List<Location> locationList) {
//        this.locationList = locationList;
//    }
//
//    public Set<Location> getLocationSet() {
//        return locationSet;
//    }
//
//    public void setLocationSet(Set<Location> locationSet) {
//        this.locationSet = locationSet;
//    }
//
//    public Collection<Location> getLocationCollection() {
//        return locationCollection;
//    }
//
//    public void setLocationCollection(Collection<Location> locationCollection) {
//        this.locationCollection = locationCollection;
//    }
}
