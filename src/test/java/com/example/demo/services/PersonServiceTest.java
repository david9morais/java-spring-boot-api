package com.example.demo.services;

import com.example.demo.exceptions.RequiredObjectIsNullException;
import com.example.demo.model.Person;
import com.example.demo.repositories.PersonRepository;
import com.example.demo.unittests.mapper.mocks.MockPerson;
import com.example.demo.valueobject.PersonVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        Person entity = input.mockEntity();
        entity.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address Test0", result.getAddress());
        assertEquals("First Name Test0", result.getFirstName());
        assertEquals("Last Name Test0", result.getLastName());
        assertEquals("Male", result.getGender());
    }

    @Test
    void findAll() {
        List<Person> list = input.mockEntityList();

        Mockito.when(repository.findAll()).thenReturn(list);

        var people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        var personOne = people.get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getKey());
        assertNotNull(personOne.getLinks());
        assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());

        var personFour = people.get(4);

        assertNotNull(personFour);
        assertNotNull(personFour.getKey());
        assertNotNull(personFour.getLinks());
        assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
        assertEquals("Address Test4", personFour.getAddress());
        assertEquals("First Name Test4", personFour.getFirstName());
        assertEquals("Last Name Test4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());

        var personSeven = people.get(7);

        assertNotNull(personSeven);
        assertNotNull(personSeven.getKey());
        assertNotNull(personSeven.getLinks());
        assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
        assertEquals("Address Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Last Name Test7", personSeven.getLastName());
        assertEquals("Female", personSeven.getGender());
    }

    @Test
    void create() {
        Person entity = input.mockEntity();
        entity.setId(1L);

        PersonVO vo = input.mockVO();
        vo.setKey(1L);

        Mockito.when(repository.save(Mockito.any())).thenReturn(entity);

        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address Test0", result.getAddress());
        assertEquals("First Name Test0", result.getFirstName());
        assertEquals("Last Name Test0", result.getLastName());
        assertEquals("Male", result.getGender());
    }

    @Test
    void createWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });
        String expectedMessage = "It's not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Person entity = input.mockEntity();
        entity.setId(1L);

        PersonVO vo = input.mockVO();
        vo.setKey(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(repository.save(Mockito.any())).thenReturn(entity);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address Test0", result.getAddress());
        assertEquals("First Name Test0", result.getFirstName());
        assertEquals("Last Name Test0", result.getLastName());
        assertEquals("Male", result.getGender());
    }

    @Test
    void updateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });
        String expectedMessage = "It's not allowed to persist a null object";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void delete() {
        Person entity = input.mockEntity();
        entity.setId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}