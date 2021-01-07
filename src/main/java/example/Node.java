package example;

public class Node implements Runnable{
    private final Resource resource;
    private int numIter;
    Node(Resource resource, int numIter) {
        this.resource = resource;
        this.numIter = numIter;
    }
    public void run() {
        for (int i = 0; i < numIter; i++) {
            resource.getAccess();
        }
    }
}
