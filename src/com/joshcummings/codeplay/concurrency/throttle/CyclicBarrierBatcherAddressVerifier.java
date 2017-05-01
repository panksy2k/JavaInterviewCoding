package com.joshcummings.codeplay.concurrency.throttle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.joshcummings.codeplay.concurrency.Address;
import com.joshcummings.codeplay.concurrency.AddressVerifier;

public class CyclicBarrierBatcherAddressVerifier implements AddressVerifier {
    private static final int NUMBER_OF_ADDRESSES = 400;
    private static ExecutorService clientPool = Executors.newCachedThreadPool();
    private static AtomicInteger rejectedCount = new AtomicInteger();
    private static CountDownLatch cdl = new CountDownLatch(NUMBER_OF_ADDRESSES);
    private static final int ARRIVAL_DELAY = 2;


    private static AddressVerifier av = new AddressVerifier() {
        @Override
        public void verify(List<Address> addresses) {
            System.out.println("Verifying addresses of the size " + addresses.size());

            for (int i = 0; i < 250; i++) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // just move on
                }
            }

            addresses.forEach((address) -> {
                address.setVerified(true);
            });
        }
    };

    private CyclicBarrierBatcher batcher;

    public CyclicBarrierBatcherAddressVerifier(AddressVerifier delegate, int batchSize, int timeout) {
        batcher = new CyclicBarrierBatcher(batchSize, timeout, delegate);
    }

    @Override
    public void verify(List<Address> addresses) {
        try {
            batcher.submit(addresses).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        CyclicBarrierBatcherAddressVerifier cbbav = new CyclicBarrierBatcherAddressVerifier(av, 100, 50);

        Random rand = new Random(676325345568L);
        int total = 0;
        long arrivalStart = System.nanoTime();

        while (total < NUMBER_OF_ADDRESSES) {
            int maxHowMany = 5;
            int howMany = rand.nextInt(maxHowMany) + 1;

            List<Address> addresses = new ArrayList<>(howMany);
            for (int j = 0; j < howMany; j++) {
                addresses.add(mockAddress(total));
            }

            total += howMany;

            if ( ARRIVAL_DELAY > 0 ) {
                try {
                    Thread.sleep(rand.nextInt(ARRIVAL_DELAY));
                } catch ( InterruptedException e ) {

                }
            }

            System.out.println("Sending " + howMany + " addresses now");
            clientPool.submit(() -> {
                long time = System.nanoTime();
                try {
                    cbbav.verify(addresses);
                } catch (Throwable e) {
                    for (int i = 0; i < addresses.size(); i++) {
                        rejectedCount.incrementAndGet();
                        cdl.countDown();
                    }
                }
            });
        }

        try {
            cdl.await();
        } catch (InterruptedException e) {
            System.out.println("Couldn't wait");
        }

        av.close();
    }


    private static Address mockAddress(int id) {
        return new Address(id + ": asdf", "asdf", "asdf", "asdf");
    }
}
