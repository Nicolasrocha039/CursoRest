package br.rs.nrfigueiredo.rest;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Data
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    Long id;
    String name;
    int age;
    Long salary;


    public User(String name, int age){
        setName(name);
        setAge(age);
    }

}
