package Client;

public class ClientRunner {
    public static void main(String[] args) {
        try {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(new Client(i + 1, true, 15, 250, 5000, 67));
                threads[i].start();
            }
            for (int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            System.err.println("Client interrupted");
            e.printStackTrace();
        }
    }
}
