package ua.com.alxkm.patterns.locks;

import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

/**
 * Demonstrates the usage of AbstractQueuedLongSynchronizer in a multithreaded environment.
 * <p>
 * AbstractQueuedLongSynchronizer (AQS) is the basis for most synchronizers in Java's java.util.concurrent package.
 */
public class AbstractQueuedLongSynchronizerExample {

    // Define a custom synchronizer extending AbstractQueuedLongSynchronizer
    static class CustomSynchronizer extends AbstractQueuedLongSynchronizer {

        // Define state constants
        private static final long LOCKED = 1;
        private static final long UNLOCKED = 0;

        // Override the tryAcquire method to acquire the lock
        @Override
        protected boolean tryAcquire(long arg) {
            // Atomically compare-and-set the state to locked
            return compareAndSetState(UNLOCKED, LOCKED);
        }

        // Override the tryRelease method to release the lock
        @Override
        protected boolean tryRelease(long arg) {
            // Atomically set the state to unlocked
            setState(UNLOCKED);
            return true;
        }
    }

    /**
     * We define a custom synchronizer CustomSynchronizer by extending AbstractQueuedLongSynchronizer and implementing tryAcquire and tryRelease methods to control the locking mechanism.
     * We create an instance of CustomSynchronizer.
     * We define two worker threads: lockThread and tryLockThread.
     * lockThread tries to acquire the lock using acquire() method and releases it using release() method.
     * tryLockThread tries to acquire the lock using tryAcquire() method and releases it if acquired, or prints a failure message if not.
     * Both threads run concurrently.
     * After both threads have completed, the main thread prints the final status.
     */
    public static void main(String[] args) {
        // Create an instance of CustomSynchronizer
        CustomSynchronizer synchronizer = new CustomSynchronizer();

        // Define worker threads
        Thread lockThread = new Thread(() -> {
            synchronizer.acquire(1); // Acquire the lock
            System.out.println(Thread.currentThread().getName() + " acquired the lock.");
            synchronizer.release(1); // Release the lock
            System.out.println(Thread.currentThread().getName() + " released the lock.");
        });

        Thread tryLockThread = new Thread(() -> {
            boolean acquired = synchronizer.tryAcquire(1); // Try to acquire the lock
            if (acquired) {
                System.out.println(Thread.currentThread().getName() + " acquired the lock.");
                synchronizer.release(1); // Release the lock if acquired
                System.out.println(Thread.currentThread().getName() + " released the lock.");
            } else {
                System.out.println(Thread.currentThread().getName() + " failed to acquire the lock.");
            }
        });

        // Start worker threads
        lockThread.start();
        tryLockThread.start();

        // Wait for worker threads to complete
        try {
            lockThread.join();
            tryLockThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
