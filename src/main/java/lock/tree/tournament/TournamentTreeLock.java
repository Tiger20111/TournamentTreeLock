package lock.tree.tournament;

import lock.tree.tournament.local.PetersonLock;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TournamentTreeLock implements Lock {
    private final ArrayList<PetersonLock> treeLocks;
    private final int sizeTree;
    public TournamentTreeLock(int numThreads) {
        treeLocks = new ArrayList<>(); //warn
        for (int i = 0; i < numThreads; i++) {
            treeLocks.add(new PetersonLock());
        }
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
            int parentId = getParentIndex(currentLock);
            treeLocks.get(parentId).lock(currentLock % 2);
            currentLock = getParentIndex(currentLock);
        }
    }

    private void unlockTree(int indexLock) {
        int way = 1;
        int steps = 0;
        int currentLock = getThreadLeaf(indexLock);
        while (currentLock > 0) {
            int childLock = currentLock;
            currentLock = getParentIndex(currentLock);
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
            treeLocks.get(currentLock).unlock(childLock & 1);
            currentLock = childLock;
            steps--;
        }
    }

}
