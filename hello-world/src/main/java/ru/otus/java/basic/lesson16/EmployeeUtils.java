package ru.otus.java.basic.lesson16;

import java.util.ArrayList;
import java.util.List;

public class EmployeeUtils {


    public static List<String> getNames(List<Employee> employees) {
        List<String> nameList = new ArrayList<>();
        for (Employee emp : employees) {
            nameList.add(emp.getName());
        }
        return nameList;
    }

    public static List<String> getNamesByMinAge(List<Employee> employees, int minAge) {
        List<String> employeesList = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.getAge() >= minAge) {
                employeesList.add(emp.getName());
                employeesList.add(String.valueOf(emp.getAge()));
            }
        }
        return employeesList;
    }

    public static void checkAvgAge(List<Employee> employees, int minAvgAge) {
        if (employees.isEmpty()) {
            System.out.println("Список сотрудников пуст");
            return;
        }
        int sumAge = 0;
        for (Employee emp : employees) {
            sumAge += emp.getAge();
        }
        double avgAge = (double) sumAge / employees.size();
        if (avgAge > minAvgAge) {
            System.out.printf("Средний возраст сотрудников: %.2f больше минимального: %d%n", avgAge, minAvgAge);
        } else {
            System.out.printf("Средний возраст сотрудников: %.2f меньше или равен минимальному: %d%n", avgAge, minAvgAge);
        }
    }

    public static int youngEmployeeIndex(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return -1;
        }
        int minAge = employees.get(0).getAge();
        int index = 0;
        for (int i = 1; i < employees.size(); i++) {
            if (employees.get(i).getAge() < minAge) {
                minAge = employees.get(i).getAge();
                index = i;
            }
        }
        return index;
    }
}