package com.pankaj.multithreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pankajpardasani on 20/03/2017.
 */
public class TickerProducerConsumer {
    private List<String> tickersQueue = new java.util.ArrayList<>(1);

    private Lock lock = new ReentrantLock();
    private Condition isEmpty = lock.newCondition();
    private Condition isFull = lock.newCondition();

    private ExecutorService service = Executors.newWorkStealingPool(2);
    private final List<Callable<String>> callables = new ArrayList<>();

    public void addTicker(List<String> tickers) {
        callables.add(new Producer(tickers));
        callables.addAll(Arrays.asList(new Consumer(), new Consumer(), new Consumer(), new Consumer()));

        try {
            List<Future<String>> references = service.invokeAll(callables);
            references.forEach(f -> {
                try {
                    f.get(1, TimeUnit.SECONDS);
                }
                catch(ExecutionException | TimeoutException | InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        finally {
            service.shutdown();
        }
    }

    private class Producer implements Callable<String> {
        List<String> tickers;

        Producer(List<String> tickerList) {
            this.tickers = tickerList;
        }

        @Override
        public String call() throws Exception {
            for (String t : tickers) {
                try {
                    lock.lock();
                    while(isFull()) {
                        isFull.await();
                    }

                    tickersQueue.add(t);
                    System.out.println("Produced "+t);
                    isEmpty.signalAll();
                } finally {
                    lock.unlock();
                }
            }

            return null;
        }
    }

    private class Consumer implements Callable<String> {
        @Override
        public String call() throws Exception {
            String tickerName;

            try{
                lock.lock();
                while(isEmpty()) {
                    if(!isEmpty.await(10, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Nothing to consume -- timeout");
                    }
                }

                tickerName = tickersQueue.remove(tickersQueue.size() - 1);
                System.out.println("Consuming "+tickerName);
                isFull.signalAll();
            }
            finally {
                lock.unlock();
            }

            return null;
        }
    }

    private boolean isFull() {
        return this.tickersQueue.size() == 2;
    }

    private boolean isEmpty() {
        return this.tickersQueue.size() == 0;
    }

    public static void main(String[] args) {
        new TickerProducerConsumer().addTicker(Arrays.asList("APL", "MS", "JPM", "STX"));
    }
}
