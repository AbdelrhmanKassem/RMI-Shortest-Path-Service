package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import Registry.IGraphService;

public class Client extends Thread {
    private int clientID;
    private int maxNumberOfBatches;
    private int maxNumberOfOperations;
    private int maxNumberOfGraphNodes;
    private int writePercentage;
    private boolean deterministic;

    public Client(int clientID, boolean deterministic) {
        this.clientID = clientID;
        this.deterministic = deterministic;
        this.maxNumberOfBatches = 100;
        this.maxNumberOfOperations = 15;
        this.maxNumberOfGraphNodes = 15;
        this.writePercentage = 67;
    }

    public Client(int clientID, boolean deterministic, int maxNumberOfBatches, int maxNumberOfOperations,
            int maxNumberOfGraphNodes,
            int writePercentage) {
        this.clientID = clientID;
        this.deterministic = deterministic;
        this.maxNumberOfBatches = maxNumberOfBatches;
        this.maxNumberOfOperations = maxNumberOfOperations;
        this.maxNumberOfGraphNodes = maxNumberOfGraphNodes;
        this.writePercentage = writePercentage;
    }

    public void run() {
        try {
            System.out.println("ClientID: " + clientID + " started");
            IGraphService graphService = (IGraphService) Naming.lookup("rmi://localhost:1099/GraphService");
            Random randomGenerator = new Random();
            int numberOfBatches = deterministic ? maxNumberOfBatches
                    : randomGenerator.nextInt(maxNumberOfBatches) + 1;
            for (int i = 0; i < numberOfBatches; i++) {
                generateAndExecuteBatch(graphService);
                Thread.sleep(randomGenerator.nextInt(10000));
            }

        } catch (MalformedURLException | RemoteException | NotBoundException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void generateAndExecuteBatch(IGraphService graphService) throws RemoteException {
        Random randomGenerator = new Random();
        int numberOfOperations = deterministic ? maxNumberOfOperations
                : randomGenerator.nextInt(maxNumberOfOperations) + 1;
        int numberOfGraphNodes = deterministic ? maxNumberOfGraphNodes
                : randomGenerator.nextInt(maxNumberOfGraphNodes) + 1;
        BatchManager batchManager = new BatchManager(clientID, numberOfOperations, numberOfGraphNodes, writePercentage);
        String batchRequest = batchManager.generateBatch();
        long startTime = System.nanoTime();
        String batchResult = graphService.executeBatch(batchRequest);
        long endTime = System.nanoTime();
        batchManager.logBatchResult(batchResult, endTime - startTime);
    }
}