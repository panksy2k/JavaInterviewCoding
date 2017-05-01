package com.pankaj.multithreading.producerConsumer;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pankajpardasani on 26/04/2017.
 */
public class WaitNotifyAlphabets {

    private static class ProducerConsumerAlphabets {
        private char movingAlphabet;
        private volatile boolean writable = false;

        void produce(char alphabet) {
            synchronized (this) {
                while (!writable) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                movingAlphabet = alphabet;
                writable = false;
                notify();
            }
        }

        char consume() {
            synchronized (this) {
                while (writable) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                writable = true;
                notify();
                return movingAlphabet;
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerAlphabets pc = new ProducerConsumerAlphabets();

        Runnable t1 = () -> {
            for (char c = 'a'; c <= 'z'; c++) {
                synchronized (pc) {
                    pc.produce(c);
                    System.out.println("Produced " + c);
                }
            }
        };

        Runnable t2 = () -> {
            char fetchedAlphabet;

            do {
                synchronized (pc) {
                    fetchedAlphabet = pc.consume();
                    System.out.println("Consumed " + fetchedAlphabet);
                }
            }
            while (fetchedAlphabet != 'z');
        };

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(t1);
        service.submit(t2);

        service.shutdown();
    }
}
