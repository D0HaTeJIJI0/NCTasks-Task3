package dto;

import lombok.Data;

import java.security.PublicKey;

@Data
public class Employer {

    private String name;
    private String surname;
    private int age;

    public Employer(String name, String surname, int age) {
        this.name = name;
        this.surname = surname;
        this.age = age;
    }
}
