package org.raml.jaxrs.codegen.test.jaxb;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.HashMap;

@XmlRootElement
@XmlType(name = "person")
public class Person {
    private Collection<String> hobby;
    private HashMap<String,Company> experiences;
    private String name;
    private String country;
    private String no;
    private byte age = 25;
    private Gender gender = Gender.FAMALE;

    public HashMap<String, Company> getExperiences() {
        return experiences;
    }

    public void setExperiences(HashMap<String, Company> experiences) {
        this.experiences = experiences;
    }

    public Collection<String> getHobby() {
        return hobby;
    }

    public void setHobby(Collection<String> hobby) {
        this.hobby = hobby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
