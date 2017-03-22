package com.pankaj.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by pankajpardasani on 22/03/2017.
 */
public class SequencingOnCyclicBarrier {
    private final ExecutorService service = Executors.newFixedThreadPool(5);
    private CyclicBarrier barrier = new CyclicBarrier(5);
    private List<Future<String>> promisesForTask = new ArrayList<>();

    public void watchMovieWithFriends() {
        for (int i = 1; i < 6; i++) {
            promisesForTask.add(service.submit(new Friend(barrier)));
        }

        promisesForTask.forEach(fut -> {
            try {
                fut.get(10, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                System.out.println("Movie programme is cancelled");
                fut.cancel(true);
            } catch (InterruptedException | ExecutionException i) {
                Thread.currentThread().interrupt();
                System.out.println("Movie programme is cancelled");
            } finally {
                service.shutdown();
            }
        });
    }

    private static class Friend implements Callable<String> {
        CyclicBarrier replyOnBarrier;

        Friend(CyclicBarrier c1) {
            replyOnBarrier = c1;
        }

        @Override
        public String call() throws Exception {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + " is outside the cinema and is waiting for others ");
                replyOnBarrier.await(2, TimeUnit.SECONDS);

                System.out.println("Ok " + Thread.currentThread().getName() + " has arrived!");
                return "Ok";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "NotOk";
            }
        }
    }

    public static void main(String[] args) {
        SequencingOnCyclicBarrier scb = new SequencingOnCyclicBarrier();
        scb.watchMovieWithFriends();
    }
}
