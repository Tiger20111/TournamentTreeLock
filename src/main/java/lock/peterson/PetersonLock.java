package lock.peterson;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PetersonLock {
    private AtomicBoolean[] flagWant;
    private volatile int victim;
    PetersonLock() {
        this.flagWant = new AtomicBoolean[2];
        this.flagWant[0].set(false);
        this.flagWant[1].set(false);
    }

    public void lock(int currentId) {
        this.flagWant[currentId].set(true);
        victim = currentId;

        while (this.flagWant[1 - currentId].get() && victim == currentId) {

        }
    }

    public void unlock(int currentId) {
        this.flagWant[currentId].set(false);
    }
}
