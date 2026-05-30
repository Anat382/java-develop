package ru.otus.java.basic.lessson17;

public class MainApp {
    public static void main(String[] args) {

        PhoneBook phoneBook = new PhoneBook();

        phoneBook.add("Иванов А.А.", "89236549858, 89165552233");
        phoneBook.add("Петров В.В.", "89231234567, 89161112233");
        phoneBook.add("Сидоров Д.М.", "89375648912");
        phoneBook.add("Кузнецова Е.А.", "89031234567, 89265678901, 89181234567");
        phoneBook.add("Смирнов П.И.", "89269876543, 89153456789");
        phoneBook.add("Васильева О.Н.", "89374567890");
        phoneBook.add("Михайлов С.Р.", "89237654321, 89165678901");
        phoneBook.add("Новикова Т.Л.", "89098765432, 89371234567");
        phoneBook.add("Федоров А.К.", "89233456789, 89233456789");
        phoneBook.add("Федоров Е.К.", "89233556789, 89374542690");
        phoneBook.add("Морозова И.Д.", "89375641234, 89162345678, 89234567890");
        phoneBook.add("Морозов И.Д.", "89375641234, 89168345678");
        phoneBook.add("Михайлова И.Д.", "89098765432");

        System.out.println("\nТелефонный справочник");
        phoneBook.printPhoneBook();

        System.out.println("\nПоиск контакта в справочнике");
        phoneBook.find("Федоров А.К.");
        phoneBook.find("Федоров");
        phoneBook.find("Сидоров");
        phoneBook.find("Войченко");

        System.out.println("\nПроверка наличия номеров");
        System.out.println("Проверка номера 89233456789: " + phoneBook.containsPhoneNumber("89233456789"));
        System.out.println("Проверка номера 99999999999: " + phoneBook.containsPhoneNumber("99999999999"));

        System.out.println("\nВладелец номера 89098765432: " + phoneBook.findOwnerByPhoneNumber("89098765432"));
    }
}