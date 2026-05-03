package ru.otus.java.basic.lesson2;

import java.time.LocalDate;
import java.time.Period;

public class User {
    public String surName;
    public String firstName;
    public String secondName;
    public int yearBerthDay;
    public String email;

    public User(String surName, String firstName, String secondName, String yearBerthDay, String email) {
        this.surName = surName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.yearBerthDay = Integer.parseInt(yearBerthDay);
        this.email = email;
    }

    public int getAge() {
        LocalDate birthDate = LocalDate.of(yearBerthDay, 1, 1);
        LocalDate today = LocalDate.now();
        int ageUser = Period.between(birthDate, today).getYears();
        return ageUser;
    }

    public void getInfo() {
        System.out.println("\nФИО: " + surName + " " + firstName + " " + secondName);
        System.out.println("Год рождения: " + yearBerthDay + " " + "рожднения");
        System.out.println("e-mail: " + email);
    }

}
