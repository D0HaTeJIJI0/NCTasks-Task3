package service;

import dao.DefaultPersonDao;
import dao.PersonDao;
import dto.Employer;
import entity.Person;
import exception.NotPersonException;
import exception.PersonNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static entity.PersonFactory.createPerson;

public class DefaultPersonService implements PersonService{
    private PersonDao personDao;

    public DefaultPersonService() {
        personDao = new DefaultPersonDao();
    }

    public DefaultPersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public UUID save(Employer employer){
        Person person = createPerson(employer);
        String id = personDao.savePerson(person);
        return  UUID.fromString(id);
    }

    @Override
    public List<UUID> saveAll(List<Employer> employerList) {
        List<Person> personList = new ArrayList<>(employerList.size());
        for (Employer employer: employerList) {
            personList.add(createPerson(employer));
        }
        List<String> listIdString = personDao.saveAllPersons(personList);
        List<UUID> uuidList = new ArrayList<>(listIdString.size());
        for (String idString: listIdString) {
            uuidList.add(UUID.fromString(idString));
        }
        return uuidList;
    }

    @Override
    public Person get(UUID uuid){
        Person person = null;
        try{
            person = personDao.getPerson(uuid.toString());
        } catch (PersonNotFoundException | NotPersonException e){
            System.err.println("ERROR");
        }
        return person;
    }

    @Override
    public List<Person> findAll() {
        return personDao.findAllPersons();
    }

    @Override
    public List<Person> findByName(String name) {
        return personDao.findPersonByName(name);
    }

    @Override
    public List<Person> findByAge(int age) {
        return personDao.findPersonByAge(age);
    }

    @Override
    public List<Person> findByNameAndAge(String name, int age) {
        return personDao.findPersonByNameAndAge(name, age);
    }

    @Override
    public void deleteById(UUID id) {
        try {
            personDao.deletePersonById(id.toString());
        } catch (PersonNotFoundException | NotPersonException e) {
            System.err.println("ERROR");
        }
    }

    @Override
    public int deleteByName(String name) {
        try {
            return personDao.deletePersonByName(name);
        } catch (PersonNotFoundException e) {
            System.err.println("ERROR");
        }
        return 0;
    }

    @Override
    public int deleteAll() {
        try {
            return personDao.deleteAllPersons();
        } catch (PersonNotFoundException e) {
            System.err.println("ERROR");
        }
        return 0;
    }
}
