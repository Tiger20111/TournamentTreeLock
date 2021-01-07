package example;

public class Node implements Runnable{
    private Resource resource;
    Node(Resource resource) {
        this.resource = resource;
    }
    public void run() {
        for (int i = 0; i < 10; i++) {
            resource.getAccess();
        }
    }
}
