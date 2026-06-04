package ru.otus.java.basic.lesson18;

public class Person {
    private String name;
    private Position position;
    private Long id;

    public Person(String name, Position position, Long id) {
        this.name = name;
        this.position = position;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Person{id=%d, name='%s', position=%s}", id, name, position);
    }
}