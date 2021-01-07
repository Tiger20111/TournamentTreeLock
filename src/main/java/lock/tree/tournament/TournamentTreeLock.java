package lock.tree.tournament;

import lock.peterson.PetersonLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TournamentTreeLock implements Lock {
    private final PetersonLock[] treeLocks;
    private final int sizeTree;
    public TournamentTreeLock(int numThreads) {
        treeLocks = new PetersonLock[numThreads - 1]; //warn
        sizeTree = numThreads;
    }

    public void lock() {
        int indexThread = (int) Thread.currentThread().getId() % sizeTree;
        lockTree(indexThread);
    }

    public void unlock() {
        int indexThread = (int) Thread.currentThread().getId() % sizeTree;
        unlockTree(indexThread);
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public Condition newCondition() {
        return null;
    }

    private void lockTree(int indexLock) {
        int currentLock = getThreadLeaf(indexLock);

        while (currentLock > 0) {
            treeLocks[getParentIndex(currentLock)].lock(currentLock % 2);
            currentLock = getParentIndex(currentLock);
        }
    }

    private void unlockTree(int indexLock) {
        int way = 1;
        int steps = 0;
        int currentLock = getThreadLeaf(indexLock);
        while (currentLock > 0) {
            int childLock = currentLock;
            currentLock = getThreadLeaf(currentLock);
            way = saveWayUnlock(childLock, currentLock, way);
            steps++;
        }

        unlockLocksByWay(steps, way, currentLock);
    }

    private int getThreadLeaf(int indexLock) {
        return indexLock + sizeTree;
    }

    private int getParentIndex(int indexLock) {
        return (indexLock - 1) / 2;
    }

    private int getLeftChild(int indexLock) {
        return 2 * indexLock + 1;
    }

    private int getRightChild(int indexLock) {
        return 2 * indexLock + 2;
    }

    private int saveWayUnlock(int childLock, int currentLock, int way) {
        way <<= 1;
        if (2 * currentLock + 2 == childLock) {
            way |= 1;
        }
        return way;
    }

    private void unlockLocksByWay(int steps, int way, int currentLock) {
        int childLock;
        while (steps > 0) {
            if ((way & 1) == 1) {
                way >>= 1;
                childLock = getRightChild(currentLock);
            } else {
                way >>= 1;
                childLock = getLeftChild(currentLock);
            }
            treeLocks[currentLock].unlock(childLock & 1);
            currentLock = childLock;
            steps--;
        }
    }

}
