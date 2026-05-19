package ru.otus.java.basic.lesson13;

import ru.otus.java.basic.lesson13.Location.DenseForest;
import ru.otus.java.basic.lesson13.Location.Plain;
import ru.otus.java.basic.lesson13.Location.Swamp;
import ru.otus.java.basic.lesson13.Vehicle.*;

public class MainApp {

    public static void main(String[] args) {

        Human human = new Human("Ivan");

        ActionVehicle[] actionVehicle = {
                new Bike(TypeTransport.BikeTp.getType(), 100_000_000),
                new Car(TypeTransport.CarTp.getType(), 60, 9),
                new CrossVehicle(TypeTransport.CrossVehicleTp.getType(), 40, 15),
                new Horse(TypeTransport.HorseTp.getType(), 100_0000, 2),
                new Walk(TypeTransport.WalkTp.getType(), 100)
        };
        Actions[] actions = {
                new DenseForest("Густой лес", human, 100_000),
                new Plain("Равнина", human, 300_000),
                new Swamp("Болото", human, 40_000)
        };

        for (ActionVehicle c : actionVehicle) {
            for (Actions o : actions) {
                human.setCurrentTransport(c.getName());
                o.doIt(c);
            }
        }
    }
}

