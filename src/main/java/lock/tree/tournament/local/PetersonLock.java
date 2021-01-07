package lock.tree.tournament.local;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class PetersonLock {
    private final AtomicIntegerArray flagWant = new AtomicIntegerArray(2);
    private volatile int victim;
    public PetersonLock() {
        this.flagWant.set(0, 0);
        this.flagWant.set(1, 0);
    }

    public void lock(int currentId) {
        this.flagWant.set(currentId, 1);
        victim = currentId;

        while (flagWant.get(1 - currentId) == 1 && victim == currentId) {

        }
    }

    public void unlock(int currentId) {
        this.flagWant.set(currentId, 0);
    }
}
