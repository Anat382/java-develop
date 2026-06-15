package ru.otus.java.basic.lesson21;


public class MainApp {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("\nРасчёт в один поток");
        long startMs = System.currentTimeMillis();
        double[] singleThreadArray = createArray();
        System.out.println(singleThreadArray.length);
        long endMs = System.currentTimeMillis();
        prinLimit(singleThreadArray, 0);
        System.out.println("Время выполнения (ms): " + (endMs - startMs) + " мс");


        System.out.println("\nРасчёт в 4 потока");
        Calculate calculate = new Calculate();
        double[] valuesArray = new double[100_000_000];
        int quarter = valuesArray.length / 4;
        Thread calcPart1 = new Thread(() -> {
            for (int i = 0; i < quarter; i++) {
                valuesArray[i] = calculate.calc(i);
            }
        });
        Thread calcPart2 = new Thread(() -> {
            for (int i = quarter; i < 2 * quarter; i++) {
                valuesArray[i] = calculate.calc(i);
            }
        });

        Thread calcPart3 = new Thread(() -> {
            for (int i = 2 * quarter; i < 3 * quarter; i++) {
                valuesArray[i] = calculate.calc(i);
            }
        });

        Thread calcPart4 = new Thread(() -> {
            for (int i = 3 * quarter; i < valuesArray.length; i++) {
                valuesArray[i] = calculate.calc(i);
            }
        });

        startMs = System.currentTimeMillis();
        calcPart1.start();
        calcPart2.start();
        calcPart3.start();
        calcPart4.start();
        calcPart1.join();
        calcPart2.join();
        calcPart3.join();
        calcPart4.join();
        endMs = System.currentTimeMillis();
        prinLimit(valuesArray, 0);
        prinLimit(valuesArray, quarter);
        prinLimit(valuesArray, 2 * quarter);
        prinLimit(valuesArray, 3 * quarter);
        System.out.println("Время выполнения (ms): " + (endMs - startMs) + " мс");

    }

    public static double[] createArray() {
        double[] valueArray = new double[100_000_000];

        for (int i = 0; i < valueArray.length; i++) {
            valueArray[i] = 1.14 * Math.cos(i) * Math.sin(i * 0.2) * Math.cos(i / 1.2);
        }
        return valueArray;
    }

    static class Calculate {
        public static double calc(double elem) {
            ;
            return 1.14 * Math.cos(elem) * Math.sin(elem * 0.2) * Math.cos(elem / 1.2);
        }
    }

    static void prinLimit(double[] valuesArray, Integer startIndex) {
        System.out.println("Проверка первых 10 элементов:");
        for (int i = startIndex; i >= startIndex && i < startIndex + 10; i++) {
            System.out.println("[" + i + "] = " + valuesArray[i]);
        }
    }
}
