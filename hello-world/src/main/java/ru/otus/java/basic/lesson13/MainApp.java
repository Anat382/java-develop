package ru.otus.java.basic.lesson13;

import ru.otus.java.basic.lesson13.Vehicle.*;

public class MainApp {

    public static void main(String[] args) {

        Transport[] transport = {
                new Bike(TypeTransport.bike, 100_000_000, 1_000, Terrain.swamp),
                new Bike(TypeTransport.bike, 100_000_000, 100_000, Terrain.plain),
                new Bike(TypeTransport.bike, 100_000_000, 10_000, Terrain.denseForest),

                new Car(TypeTransport.car, 60, 9, 1_000, Terrain.swamp),
                new Car(TypeTransport.car, 60, 9, 500_000, Terrain.plain),
                new Car(TypeTransport.car, 60, 9, 100_000, Terrain.denseForest),

                new CrossVehicle(TypeTransport.crossVehicle, 40, 20, 100_000, Terrain.swamp),
                new CrossVehicle(TypeTransport.crossVehicle, 40, 15, 300_000, Terrain.plain),
                new CrossVehicle(TypeTransport.crossVehicle, 40, 18, 200_000, Terrain.denseForest),

                new Horse(TypeTransport.horse, 100_0000, 2, 5_000, Terrain.swamp),
                new Horse(TypeTransport.horse, 100_0000, 2, 100_000, Terrain.plain),
                new Horse(TypeTransport.horse, 100_0000, 2, 50_000, Terrain.denseForest),
        };

        Human[] human = {
                new Human("Ivan", TypeTransport.bike, 100_000),
                new Human("Ivan", TypeTransport.car, 100_000),
                new Human("Ivan", TypeTransport.crossVehicle, 100_000),
                new Human("Ivan", TypeTransport.horse, 100_000),
                new Human("Ivan", TypeTransport.none, 100_000),
        };

        for (Human o : human) {
            o.getInfo();
            for (Transport c : transport) {
                if (o.hasCurrentTransport()) {
                    o.move(c);
                } else {
                    o.move(5_000);
                    break;
                }

            }
        }
    }
}

