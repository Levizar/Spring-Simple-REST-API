package com.bartoletti.person.dao;

import com.bartoletti.person.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {

    List<Person> DB = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        DB.add(new Person(id, person.getName()));
        return 1; // Always work
    }

    @Override
    public List<Person> selectAllPeople() {
        return DB;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return DB.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deletePersonById(UUID id) {
        Optional<Person> personMaybe = selectPersonById(id);
        if (personMaybe.isEmpty()) return 0;
        DB.remove(personMaybe.get());
        return 1;
    }

    // TODO: 26/05/2020 using a double loop through stream -> bad practice ?
    @Override
    public int updatePersonById(UUID id, Person person) {
        return selectPersonById(id)
                .map(p -> {
                    int indexPersonToDelete = DB.indexOf(p);
                    if (indexPersonToDelete >= 0) {
                        DB.set(indexPersonToDelete, person);
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
