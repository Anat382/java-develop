package ru.otus.java.basic.lessson17;

import java.util.*;

public class PhoneBook {
    
    private final Map<String, Set<String>> phoneBooks = new HashMap<>();
    
    public void add(String name, String phones) {
        String[] numbersArray = phones.split(",\\s*");
        Set<String> newNumbers = new HashSet<>(Arrays.asList(numbersArray));
        phoneBooks.put(name, newNumbers);
    }


    public void printPhoneBook() {
        int i = 1;
        for (Map.Entry<String, Set<String>> entry : phoneBooks.entrySet()) {
            System.out.println(i + ". " + entry.getKey() + " -> " + entry.getValue());
            i++;
        }
    }

    public void find(String namePart) {
        boolean found = false;
        for (Map.Entry<String, Set<String>> entry : phoneBooks.entrySet()) {
            if (entry.getKey().toLowerCase().contains(namePart.toLowerCase())) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
                found = true;
            }
        }
        if (!found) {
            System.out.println("В справочнике не найден контакт: " + namePart);
        }
    }

    public boolean containsPhoneNumber(String phoneNumber) {
        for (Set<String> numbers : phoneBooks.values()) {
            if (numbers.contains(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    public String findOwnerByPhoneNumber(String phoneNumber) {
        for (Map.Entry<String, Set<String>> entry : phoneBooks.entrySet()) {
            if (entry.getValue().contains(phoneNumber)) {
                return entry.getKey();
            }
        }
        return null;
    }

}