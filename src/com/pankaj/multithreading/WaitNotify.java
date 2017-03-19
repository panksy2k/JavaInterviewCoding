package com.pankaj.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pankajpardasani on 24/01/2017.
 */
public class WaitNotify {

    private static int[] elements = new int[10];
    private static int count = 0;

    private static Object lock = new Object();

    private static class Producer {
        void produce() {
            synchronized (lock) {
                if (isFull()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        //e.printStackTrace();
                    }
                }

                elements[count] = 1;
                ++count;
                System.out.println("Count (in producer) is " + elements.length);
                lock.notify();
            }
        }

        boolean isFull() {
            return count == 10 ? true : false;
        }
    }

    private static class Consumer {
        void consume() {
            synchronized (lock) {
                if (isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        //e.printStackTrace();
                    }
                }

                elements[count] = 0;
                --count;
                System.out.println("Count (in consumer) is " + elements.length);

                lock.notify();
            }
        }

        boolean isEmpty() {
            return count == 0 ? true : false;
        }
    }

    public static void main(String[] args) {
        Runnable t1 = () -> new Producer().produce();
        Runnable t2 = () -> new Consumer().consume();

        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(t1);
        service.submit(t2);

        System.out.println("elements present in buffer is: " + elements.length);

        service.shutdown();
    }
}
