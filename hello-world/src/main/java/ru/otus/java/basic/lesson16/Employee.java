package ru.otus.java.basic.lesson16;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Employee {
    private String name;
    private int age;
    private List<Employee> names = new ArrayList<>();

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Employee() {
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void addEmployee(Employee employee) {
        names.add(employee);
    }

    public List<Employee> getEmployee() {
        return names;
    }

    public List<String> getNames(List<Employee> employee) {
        List<String> nameList = new ArrayList<>();
        for (Employee nm : employee) {
            nameList.add(nm.getName());
        }
        return nameList;
    }

    public List<String> getNamesAge(List<Employee> employee, int minAge) {
        List<String> nameList = new ArrayList<>();
        for (Employee emp : employee) {
            if (emp.getAge() >= minAge) {
                nameList.add(emp.getName());
            }
        }
        return nameList;
    }

    public void checkAvgAge(List<Employee> employee, int minAvgAge) {
        int sumAge = 0;
        for (Employee emp : employee) {
            sumAge += emp.getAge();
        }

        double avgAge = (double) sumAge / employee.size();
        if (avgAge > (double) (minAvgAge)) {
            System.out.println("Средний возраст сотрудников: " + avgAge + " больше минимального среднего возраста: " + minAvgAge);
        } else {
            System.out.println("Средний возраст сотрудников: " + avgAge + " меньше минимального среднего возраста: " + minAvgAge);
        }
    }

    public int youngEmployeeIndex(List<Employee> employee) {
        int minAge = employee.get(0).getAge();
        int indexAge = 0;
        for (int i = 0; i < employee.size(); i++) {
            int ageEmp = employee.get(i).getAge();
            if (ageEmp < minAge) {
                minAge = ageEmp;
                indexAge = i;
            }
        }
        return indexAge;
    }

}
