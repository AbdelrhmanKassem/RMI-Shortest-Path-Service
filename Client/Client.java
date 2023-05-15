package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import Registry.IGraphService;

public class Client extends Thread {
    public int clientID;

    public Client(int clientID) {
        this.clientID = clientID;
    }

    public void run() {
        try {
            System.out.println("ClientID: " + clientID);
            IGraphService graphService = (IGraphService) Naming.lookup("rmi://localhost:1099/GraphService");
            Random randomGenerator = new Random();
            int numberOfBatches = randomGenerator.nextInt(100);
            for (int i = 0; i < numberOfBatches; i++) {
                System.out.println("Generating Batch #" + (i+1));
                // TODO: Generate Batch of random number of operations
                long startTime = System.nanoTime();
                String batchResult = graphService.executeBatch("Batch #" + (i+1));
                long endTime = System.nanoTime();
                // TODO: Log the result of the batch and the time
                System.out.println("ClientID: " + clientID + " Batch result: " + batchResult + " in :" + (endTime - startTime) + " nanoseconds");
                Thread.sleep(randomGenerator.nextInt(10000));
            }

        } catch (MalformedURLException | RemoteException | NotBoundException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}