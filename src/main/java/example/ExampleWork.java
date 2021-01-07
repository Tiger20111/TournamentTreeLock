package example;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExampleWork {
    ArrayList<Node> nodes = new ArrayList<>();
    Resource resource;
    int numThreads;
    public ExampleWork(int n, int numIter) {
        resource = new Resource(n);
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(resource, numIter));
        }
        numThreads = n;
    }

    public void run() {
        ArrayList<Future<?>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        for (Node node:
                nodes) {
            futures.add(executorService.submit(node));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }
}
