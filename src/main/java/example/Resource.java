package example;

import lock.tree.tournament.TournamentTreeLock;

import java.util.concurrent.locks.Lock;

public class Resource {
    Lock lock;
    Resource(int n) {
        lock = new TournamentTreeLock(n);
    }

    public void getAccess() {
        lock.lock();
        System.out.println("Thread id " + Thread.currentThread().getId() + " come in lock");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
    }
}
