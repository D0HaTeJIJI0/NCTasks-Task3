package service;

import dao.PersonDao;
import dto.Employer;
import entity.Person;
import entity.PersonFactory;
import exception.NotPersonException;
import exception.PersonNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DefaultPersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonDao personDao;

    private Employer employer;
    private Person person;
    private String id;
    private UUID uuid;
    private List<Employer> employerList;
    private List<Person> personList;
    private List<UUID> uuidList;
    private List<String> idList;
    private String[] names = {
      "Eren", "Mikasa", "Armin", "Kirigai", "Light"
    };
    private String[] surNames = {
            "Eger", "Akkerman", "Arlet", "Kazuto", "Yagami"
    };
    private int[] ages = {
      15, 14, 15, 17, 23
    };
    private final int EMPLOYER_AMOUNT = 5;

    private Executable getPersonClosure;
    private Executable deletePersonByIdClosure;
    private Executable deletePersonByNameClosure;
    private Executable deleteAllPersonClosure;

    @BeforeAll
    public void initBeforeAll() {
        personService = new DefaultPersonService(personDao);
    }

    @BeforeEach
    public void initBeforeEach() {

        employer = new Employer("Dima", "Pushkin", 30);

        employerList = new ArrayList<>(EMPLOYER_AMOUNT);
        uuidList = new ArrayList<>(EMPLOYER_AMOUNT);
        Employer tmpEmployer;
        for (int i = 0; i < EMPLOYER_AMOUNT; i++) {
            tmpEmployer = new Employer(names[i], surNames[i], ages[i]);
            employerList.add(tmpEmployer);
            personList.add(PersonFactory.createPerson(tmpEmployer));
            uuidList.add(UUID.randomUUID());
            idList.add(uuidList.get(i).toString());
        }

        uuid = UUID.randomUUID();
        id = UUID.randomUUID().toString();

        person = new Person("Alex", (byte) 25);

        getPersonClosure = new Executable() {
            @Override
            public void execute() throws Throwable {
                personDao.getPerson(uuid.toString());
            }
        };
        deletePersonByIdClosure = new Executable() {
            @Override
            public void execute() throws Throwable {
                personDao.deletePersonById(uuid.toString());
            }
        };
        deletePersonByNameClosure = new Executable() {
            @Override
            public void execute() throws Throwable { personDao.deletePersonByName(uuid.toString());
            }
        };
        deleteAllPersonClosure = new Executable() {
            @Override
            public void execute() throws Throwable { personDao.deleteAllPersons();
            }
        };

    }

    @org.junit.jupiter.api.Test
    public void saveShouldReturnNotNullUUID() {
        when(personDao.savePerson(any(Person.class))).thenReturn(id);
        UUID uuid = personService.save(employer);
        assertNotNull(uuid);
    }

    @org.junit.jupiter.api.Test
    public void saveShouldReturnProperUUID() {
        when(personDao.savePerson(any(Person.class))).thenReturn(id);
        UUID uuid = personService.save(employer);
        assertEquals(uuid.toString(), id);
    }

    @org.junit.jupiter.api.Test
    public void getShouldReturnNotNullPerson() throws NotPersonException, PersonNotFoundException {
        when(personDao.getPerson(uuid.toString())).thenReturn(person);
        Person person = personService.get(uuid);
        assertNotNull(person);
    }

    @org.junit.jupiter.api.Test
    public void getShouldReturnProperPerson() throws NotPersonException, PersonNotFoundException {
        when(personDao.getPerson(uuid.toString())).thenReturn(this.person);
        Person person = personService.get(uuid);
        assertEquals(person, this.person);
    }

    @Test
    public void getShouldThrowNotPersonException() throws NotPersonException, PersonNotFoundException {
        when(personDao.getPerson(uuid.toString())).thenThrow(NotPersonException.class);
        assertThrows(NotPersonException.class, getPersonClosure);
    }

    @Test
    public void getShouldThrowPersonNotFoundException() throws NotPersonException, PersonNotFoundException {
        when(personDao.getPerson(uuid.toString())).thenThrow(PersonNotFoundException.class);
        assertThrows(NotPersonException.class, getPersonClosure);
    }

    @Test
    void saveAllShouldReturnNotNullListOfPersonsIds() {
        when(personDao.saveAllPersons(personList)).thenReturn(idList);
        List<UUID> uuidList = personService.saveAll(employerList);
        assertNotNull(uuidList);
    }

    @Test
    void saveAllShouldReturnProperListOfPersonsIds() {
        when(personDao.saveAllPersons(personList)).thenReturn(this.idList);
        List<UUID> uuidList = personService.saveAll(employerList);
        assertEquals(uuidList, this.uuidList);
    }

    @Test
    void findAllShouldReturnNotNullListOfPersons() {
        when(personDao.findAllPersons()).thenReturn(this.personList);
        List<Person> personList = personService.findAll();
        assertNotNull(personList);
    }

    @Test
    void findAllShouldReturnProperListOfPersons() {
        when(personDao.findAllPersons()).thenReturn(this.personList);
        List<Person> personList = personService.findAll();
        assertEquals(personList, this.personList);
    }

    @Test
    void findByNameShouldReturnNotNullListOfPersons() {
        List<Person> personListExpected = new ArrayList<>(1);
        personListExpected.add(personList.get(0));
        when(personDao.findPersonByName(names[0])).thenReturn(personListExpected);
        List<Person> personListActual = personService.findByName(names[0]);
        assertNotNull(personListActual);
    }

    @Test
    void findByNameShouldReturnProperListOfPersons() {
        List<Person> personListExpected = new ArrayList<>(1);
        personListExpected.add(personList.get(0));
        when(personDao.findPersonByName(names[0])).thenReturn(personListExpected);
        List<Person> personListActual = personService.findByName(names[0]);
        assertEquals(personListExpected, personListActual);
    }

    @Test
    void findByAgeShouldReturnNotNullListOfPersons() {
        List<Person> personListExpected = new ArrayList<>(2);
        personListExpected.add(personList.get(0));
        personListExpected.add(personList.get(2));
        when(personDao.findPersonByAge(ages[0])).thenReturn(personListExpected);
        List<Person> personListActual = personService.findByAge(ages[0]);
        assertNotNull(personListActual);
    }

    @Test
    void findByAgeShouldReturnProperListOfPersons() {
        List<Person> personListExpected = new ArrayList<>(2);
        personListExpected.add(personList.get(0));
        personListExpected.add(personList.get(2));
        when(personDao.findPersonByAge(ages[0])).thenReturn(personListExpected);
        List<Person> personListActual = personService.findByAge(ages[0]);
        assertEquals(personListExpected, personListActual);
    }

    @Test
    void findByNameAndAgeShouldReturnNotNullListOfPersons() {
        List<Person> personListExpected = new ArrayList<>(1);
        personListExpected.add(personList.get(0));
        when(personDao.findPersonByNameAndAge(names[0], ages[0])).thenReturn(personListExpected);
        List<Person> personListActual = personService.findByNameAndAge(names[0], ages[0]);
        assertNotNull(personListActual);
    }

    @Test
    void findByNameAndAgeShouldReturnProperListOfPersons() {
        List<Person> personListExpected = new ArrayList<>(1);
        personListExpected.add(personList.get(0));
        when(personDao.findPersonByNameAndAge(names[0], ages[0])).thenReturn(personListExpected);
        List<Person> personListActual = personService.findByNameAndAge(names[0], ages[0]);
        assertEquals(personListExpected, personListActual);
    }

    @Test
    void deleteByIdShouldInvokeDeletePersonById() throws NotPersonException, PersonNotFoundException {
        personService.deleteById(uuid);
        Mockito.verify(personDao).deletePersonById(uuid.toString());
    }

    @Test
    void deleteByIdShouldThrowPersonNotFoundException() throws NotPersonException, PersonNotFoundException {
        doThrow(new PersonNotFoundException()).when(personDao).deletePersonById(uuid.toString());
        assertThrows(NotPersonException.class, deletePersonByIdClosure);
    }

    @Test
    void deleteByIdShouldThrowNotPersonException() throws NotPersonException, PersonNotFoundException {
        doThrow(new NotPersonException()).when(personDao).deletePersonById(uuid.toString());
        assertThrows(NotPersonException.class, deletePersonByIdClosure);
    }

    @Test
    void deleteByNameShouldReturnProperNumberOfDeletedPersons() throws PersonNotFoundException {
        when(personDao.deletePersonByName(names[0])).thenReturn(1);
        int numberOfDeleted = personService.deleteByName(names[0]);
        assertEquals(numberOfDeleted, 1);
    }

    @Test
    void deleteByNameShouldThrowPersonNotFoundException() throws PersonNotFoundException {
        when(personDao.deletePersonByName(names[0])).thenThrow(PersonNotFoundException.class);
        assertThrows(PersonNotFoundException.class, deletePersonByNameClosure);
    }

    @Test
    void deleteAllShouldReturnProperNumberOfDeletedPersons() throws PersonNotFoundException {
        when(personDao.deleteAllPersons()).thenReturn(5);
        int numberOfDeleted = personService.deleteAll();
        assertEquals(numberOfDeleted, 5);
    }

    @Test
    void deleteAllShouldThrowPersonNotFoundException() throws PersonNotFoundException {
        when(personDao.deleteAllPersons()).thenThrow(PersonNotFoundException.class);
        assertThrows(PersonNotFoundException.class, deleteAllPersonClosure);
    }
}