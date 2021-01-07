import example.ExampleWork;

public class Application {
    public static void main(String[] args) {
        int numThreads = 6;
        int iterationLock = 3;
        ExampleWork exampleWork = new ExampleWork(numThreads, iterationLock);
        exampleWork.run();
    }
}
