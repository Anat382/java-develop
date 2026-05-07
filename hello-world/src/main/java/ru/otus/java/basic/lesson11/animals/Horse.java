package ru.otus.java.basic.lesson11.animals;

public class Horse extends Animal {
    private int LossEndurance;

    public Horse(String name, float speedRun, float speedSwim, int endurance, int LossEndurance) {
        this.name = name;
        this.speedRun = speedRun;
        this.speedSwim = speedSwim;
        this.endurance = endurance;
        this.LossEndurance = LossEndurance;
    }

    @Override
    public int swim(int distance) {
        int lossTimeSeconds = -1;
        int LossEnduranceDistance = distance * LossEndurance; // 4 ед. выносливости
        if (endurance >= LossEnduranceDistance) {
            endurance = endurance - LossEnduranceDistance;
            lossTimeSeconds = (int) (distance * speedSwim);
            System.out.println("Животное проплыло: " + distance + " метров, за время " + lossTimeSeconds + " секунд, осталось выносливости: " + endurance + " ед.");
        } else if (endurance > 0){
            System.out.println("У животного не хватает выносливости проплыть дистанцию: " + distance + " метров, иначе утонет!, требуется выносливости: " + LossEnduranceDistance + " ед." + ", осталось выносливости: " + endurance  + " ед.");
        } else {
            endurance = -1;
            System.out.println("У животного нет выносливости для плавания на дистанцию: " + distance + "метров, остаток выносливости: " + endurance + " ед.");
        }
        return lossTimeSeconds;
    }

//} else if (endurance > 0 && endurance * LossEndurance < LossEnduranceDistance ){
//int maxDistanceRun = endurance * LossEndurance;
//endurance = endurance - maxDistanceRun;
//lossTimeSeconds = (int) (maxDistanceRun * speedRun);
//        System.out.println("Животное проплыло: " + maxDistanceRun + " метров, из заданных: " + distance + " метров, за время " + lossTimeSeconds + " секунд," + " осталось выносливости: " + endurance + " ед.");
}
