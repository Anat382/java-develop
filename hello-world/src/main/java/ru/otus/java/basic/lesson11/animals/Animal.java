package ru.otus.java.basic.lesson11.animals;

public abstract class Animal {
    String name;
    float speedRun;
    float speedSwim;
    int endurance;

    public int run(int distance) {
        int lossTimeSeconds = -1;
        if (endurance >= distance) {
            endurance = endurance - distance;
            lossTimeSeconds = (int) (distance * speedRun);
            System.out.println("Животное пробежало: " + distance + " метров, за время " + lossTimeSeconds + " секунд, осталось выносливости: " + endurance + " ед.");
        } else if (endurance > 0 && endurance < distance){
            int distanceRun = endurance;
            endurance = endurance - distanceRun;
            lossTimeSeconds = (int) (distanceRun * speedRun);
            System.out.println("Животное пробежало: " + distanceRun + " метров, из заданных: " + distance + " метров, за время " + lossTimeSeconds + " секунд," + " осталось выносливости: " + endurance + " ед.");
        } else {
            endurance = -1;
            System.out.println("У животного нет выносливости для бега на дистанцию: " + distance + " метров, остаток выносливости: " + endurance + " ед.");
        }
        return lossTimeSeconds;
    }

    public int swim(int distance) {
        return -1;
    }

    public void info() {
        System.out.println("\nИмя животного: " + name + ", скорость бега м/c: " + speedRun + ", скорость плавания м/c: " + speedSwim + ", заданная выносливость: " + endurance + " ед.");
        if (endurance <= 0) {
            System.out.println("Животное устало и не может двигаться: " + endurance + " ед.");
        } else {
            System.out.println("Остаток выносливости животного: " + endurance + " ед.");
        }
    }

}