package dao;

import entity.Person;
import exception.NotPersonException;
import exception.PersonNotFoundException;
import storage.TemporaryStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultPersonDao implements PersonDao {

    private TemporaryStorage storage = TemporaryStorage.getInstance();

    public DefaultPersonDao() {
    }

    public String savePerson(Person person) {
        String id = UUID.randomUUID().toString();
        person.setId(id);
        storage.insertEntity(id, person);
        return id;
    }

    public List<String> saveAllPersons(List<Person> personList) {
        List<String> idList = new ArrayList<>(personList.size());
        for (Person person: personList) {
            String id = UUID.randomUUID().toString();
            person.setId(id);
            storage.insertEntity(id, person);
            idList.add(id);
        }
        return idList;
    }

    @Override
    public List<Person> findAllPersons() {
        List<Object> entityList = storage.getAllEntities();
        List<Person> personList = new ArrayList<>(entityList.size());
        for (Object entity: entityList) {
            if (Person.class.equals(entity.getClass())) {
                personList.add((Person) entity);
            }
        }
        return personList;
    }

    @Override
    public List<Person> findPersonByName(String name) {
        List<Person> personList = findAllPersons();
        List<Person> result = new ArrayList<>();
        for (Person person: personList) {
            if (name.equals(person.getName())) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public List<Person> findPersonByAge(int age) {
        List<Person> personList = findAllPersons();
        List<Person> result = new ArrayList<>();
        for (Person person: personList) {
            if (age == (int) person.getAge()) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public List<Person> findPersonByNameAndAge(String name, int age) {
        List<Person> personList = findAllPersons();
        List<Person> result = new ArrayList<>();
        for (Person person: personList) {
            if (name.equals(person.getName())
                    && age == (int) person.getAge()) {
                result.add(person);
            }
        }
        return result;
    }

    @Override
    public void deletePersonById(String id) throws PersonNotFoundException, NotPersonException {
        Object entity = storage.getEntity(id);
        if (entity == null) {
            throw new PersonNotFoundException();
        }
        if (!Person.class.equals(entity.getClass())) {
            throw new NotPersonException();
        }
        storage.deleteEntity(id);
    }

    @Override
    public int deletePersonByName(String name) throws PersonNotFoundException {
        List<Person> personList = findAllPersons();
        int amountOfDeleted = 0;
        for (Person person: personList) {
            if (name.equals(person.getName())) {
                amountOfDeleted++;
                storage.deleteEntity(person);
            }
        }
        if (personList.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return amountOfDeleted;
    }

    @Override
    public int deleteAllPersons() throws PersonNotFoundException {
        List<Person> personList = findAllPersons();
        int amountOfDeleted = 0;
        for (Person person: personList) {
            amountOfDeleted++;
            storage.deleteEntity(person);
        }
        if (personList.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return amountOfDeleted;
    }

    public Person getPerson(String id) throws PersonNotFoundException, NotPersonException {
        Object entity = storage.getEntity(id);
        if (entity == null) {
            throw new PersonNotFoundException();
        }
        if (!Person.class.equals(entity.getClass())) {
            throw new NotPersonException();
        }
        return (Person) entity;
    }


}
