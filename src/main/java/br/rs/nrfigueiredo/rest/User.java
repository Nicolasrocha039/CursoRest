package br.rs.nrfigueiredo.rest;

import lombok.Data;


@Data
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
