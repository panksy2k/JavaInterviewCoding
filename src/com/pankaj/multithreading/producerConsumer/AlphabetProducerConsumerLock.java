package com.pankaj.multithreading.producerConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pankajpardasani on 29/04/2017.
 */
public class AlphabetProducerConsumerLock {

    public static void main(String[] args) {
        AlphabetProduceConsume alphebetManager = new AlphabetProduceConsume();

        ExecutorService service = Executors.newFixedThreadPool(2);
        List<Callable<String>> pc = Arrays.asList(new Producer(alphebetManager), new Consumer(alphebetManager));

        try {
            List<Future<String>> futures = service.invokeAll(pc);

            futures.forEach(
                    future -> {
                        try {
                            System.out.println(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            System.out.println("Exception: " + e.getMessage());
                        }
                    });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            service.shutdown();
            System.out.println("Executor service shut down");
        }

        service.shutdown();
    }

    private static class AlphabetProduceConsume {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        char alphabet;
        volatile boolean alphabetPresent = false;

        Lock getLock() {
            return this.lock;
        }

        void setAlphabet(char alphabet) {
            getLock().lock();

            try {
                while (alphabetPresent) {
                    try {
                        condition.await(3000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                this.alphabet = alphabet;
                alphabetPresent = true;
                this.condition.signal();
            } finally {
                getLock().unlock();
            }
        }

        char getAlphabet() {
            getLock().lock();

            try {
                while (!alphabetPresent) {
                    try {
                        condition.await(3000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                alphabetPresent = false;
                this.condition.signal();

            } finally {
                getLock().unlock();
            }

            return this.alphabet;
        }
    }


    private static class Producer implements Callable<String> {
        final AlphabetProduceConsume shared;

        Producer(AlphabetProduceConsume shared) {
            this.shared = shared;
        }

        @Override
        public String call() {
            for (char start = 'A'; start <= 'Z'; start++) {
                shared.getLock().lock();
                shared.setAlphabet(start);
                System.out.println("Setting the alphabet " + start);
                shared.getLock().unlock();
            }

            return "producer complete";
        }
    }

    private static class Consumer implements Callable<String> {
        AlphabetProduceConsume shared;

        Consumer(AlphabetProduceConsume shared) {
            this.shared = shared;
        }

        @Override
        public String call() {
            char tempAlphabet;

            do {
                shared.getLock().lock();
                tempAlphabet = shared.getAlphabet();
                System.out.println("Fetching the alphabet " + tempAlphabet);
                shared.getLock().unlock();
            }
            while (tempAlphabet != 'Z');

            return "consumer complete";
        }
    }
}
