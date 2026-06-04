package ru.otus.java.basic.lesson18;

import java.util.*;

public class PersonDataBase {

    private final Map<Long, Person> dataBase = new HashMap<>();

    public void add(Person person) {
        dataBase.put(person.getId(), person);
    }

    public Person findById(Long id) {
        return dataBase.get(id);
    }

    public boolean isManager(Person person) {
        Position pos = person.getPosition();
        return pos.equals(Position.MANAGER) ||
                pos.equals(Position.DIRECTOR) ||
                pos.equals(Position.BRANCH_DIRECTOR) ||
                pos.equals(Position.SENIOR_MANAGER);
    }

    public boolean isEmployee(Long id) {
        return !isManager(dataBase.get(id));
    }
}