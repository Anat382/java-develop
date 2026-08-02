package ru.otus.java.basic.lesson30;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainApp {
    private static final Object lock = new Object();
    private static int current = 0;

    public static void main(String[] args) {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        pool.submit(new PrintTask('A', 0));
        pool.submit(new PrintTask('B', 1));
        pool.submit(new PrintTask('C', 2));

        pool.shutdown();
        try {
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    static class PrintTask implements Runnable {
        private final char letter;
        private final int index;

        PrintTask(char letter, int index) {
            this.letter = letter;
            this.index = index;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                synchronized (lock) {
                    while (current != index) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    System.out.print(letter);
                    current = (current + 1) % 3;
                    lock.notifyAll();
                }
            }
        }
    }
}