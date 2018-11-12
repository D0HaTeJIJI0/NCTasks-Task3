package dao;

import entity.Person;
import exception.NotPersonException;
import exception.PersonNotFoundException;

import java.util.List;

public interface PersonDao {
    String savePerson(Person person);
    List<String> saveAllPersons(List<Person> personList);
    Person getPerson(String id) throws PersonNotFoundException, NotPersonException;
    List<Person> findAllPersons();
    List<Person> findPersonByName(String name);
    List<Person> findPersonByAge(int age);
    List<Person> findPersonByNameAndAge(String name, int age);
    void deletePersonById(String id) throws PersonNotFoundException, NotPersonException;
    int deletePersonByName(String name) throws PersonNotFoundException;
    int deleteAllPersons() throws PersonNotFoundException;
}
