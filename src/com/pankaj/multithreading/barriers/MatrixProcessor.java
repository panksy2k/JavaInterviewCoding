package com.pankaj.multithreading.barriers;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by pankajpardasani on 28/04/2017.
 */
public class MatrixProcessor {
    private final int SIZE;
    private final int[][] matrix;
    private int counter = 0;
    private final CyclicBarrier barrier;

    private ExecutorService service = Executors.newFixedThreadPool(3);

    private class MatrixChangerWorkers implements Runnable {
        int rowNumberToProcess;
        boolean done = false;

        MatrixChangerWorkers(int rowNumber) {
            rowNumberToProcess = rowNumber;
        }

        @Override
        public void run() {
            while (!done) {
                processRow();

                try {
                    //barrier.await();
                    if (barrier.await(1000, TimeUnit.MILLISECONDS) == 0 ) {
                        mergeMatrix();
                    }
                } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
                    System.out.println("Exception occurred but still merge of whatever we have got " + e.getMessage());
                    mergeMatrix();
                }
            }
        }

        public void processRow() {
            for (int j = 0; j < 3; j++) {
                matrix[rowNumberToProcess][j] *= 10;
            }

            this.done = true;
        }
    }

    public MatrixProcessor(int workers) {
        this.SIZE = workers;
        matrix = new int[workers][3];

        for (int i = 0; i < this.SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j] = counter++;
            }
        }

        barrier = new CyclicBarrier(this.SIZE);
    }

    private void dumpMatrix() {
        for (int i = 0; i < this.SIZE; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }
    }

    private void mergeMatrix() {
        System.out.println("Matrix is processed now and the latch will break. Now we shall notify main thread to print the results");

        /*synchronized ("abc") {
            "abc".notifyAll();
        }*/

        System.out.println("Printing final values");
        dumpMatrix();
    }

    public void processMatrix() {
        dumpMatrix();

        /*for (int i = 0; i < 3; i++) {
            new Thread(new MatrixChangerWorkers(i)).start();
        }*/


        service.execute(() -> new MatrixChangerWorkers(0));
        service.execute(() -> new MatrixChangerWorkers(1));
        service.execute(() -> new MatrixChangerWorkers(2));

        /*synchronized ("abc") {
            try {
                "abc".wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }*/

        try {
            service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdown();
    }

    public static void main(String[] args) {
        MatrixProcessor processor = new MatrixProcessor(3);
        processor.processMatrix();
    }
}
