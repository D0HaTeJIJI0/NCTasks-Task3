package service;

import dto.Employer;
import entity.Person;
import exception.NotPersonException;
import exception.PersonNotFoundException;

import java.util.List;
import java.util.UUID;

public interface PersonService {
    UUID save(Employer employer);
    List<UUID> saveAll(List<Employer> employerList);
    Person get(UUID uuid);
    List<Person> findAll();
    List<Person> findByName(String name);
    List<Person> findByAge(int age);
    List<Person> findByNameAndAge(String name, int age);
    void deleteById(UUID id);
    int deleteByName(String name);
    int deleteAll();
    
}
