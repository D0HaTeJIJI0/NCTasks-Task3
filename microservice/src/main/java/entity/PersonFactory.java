package entity;

import dto.Employer;

public class PersonFactory {

    private PersonFactory() {}

    public static Person createPerson(Employer employer){
        return new Person(employer.getName(), (byte)employer.getAge());
    }
}
