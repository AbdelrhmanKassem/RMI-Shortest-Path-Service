package Client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

import Registry.IGraphService;

public class Client extends Thread {
    private int clientID;
    private int numberOfBatches;
    private BatchGenerator batchGenerator;
    private LoggerManger logger;

    public Client(int clientID, boolean deterministic) {
        this.clientID = clientID;
        this.numberOfBatches = deterministic ? 10
                : new Random().nextInt(10) + 1;
        this.batchGenerator = configureBatchGenerator(10, 15, 67, deterministic);
        this.logger = new LoggerManger(clientID);
    }

    public Client(int clientID, boolean deterministic, int maxNumberOfBatches, int maxNumberOfOperations,
            int maxNumberOfGraphNodes,
            int writePercentage) {
        this.clientID = clientID;
        this.numberOfBatches = deterministic ? maxNumberOfBatches
                : new Random().nextInt(maxNumberOfBatches) + 1;
        batchGenerator = configureBatchGenerator(maxNumberOfOperations, maxNumberOfGraphNodes, writePercentage,
                deterministic);
        this.logger = new LoggerManger(clientID);
    }

    public void run() {
        try {
            System.out.println("ClientID: " + clientID + " started");
            IGraphService graphService = (IGraphService) Naming.lookup("rmi://localhost:1099/GraphService");
            for (int i = 0; i < numberOfBatches; i++) {
                generateAndExecuteBatch(graphService, i + 1);
                Thread.sleep(new Random().nextInt(9000) + 1000);
            }
        } catch (MalformedURLException | RemoteException | NotBoundException | InterruptedException e) {
            System.err.println("ClientID " + clientID + " error");
            e.printStackTrace();
        }
    }

    private BatchGenerator configureBatchGenerator(int maxNumberOfOperations, int maxNumberOfGraphNodes,
            int writePercentage, boolean deterministic) {
        Random randomGenerator = new Random();
        int numberOfOperations = deterministic ? maxNumberOfOperations
                : randomGenerator.nextInt(maxNumberOfOperations) + 1;
        int numberOfGraphNodes = deterministic ? maxNumberOfGraphNodes
                : randomGenerator.nextInt(maxNumberOfGraphNodes) + 1;
        return new BatchGenerator(numberOfOperations, numberOfGraphNodes, writePercentage);
    }

    private void generateAndExecuteBatch(IGraphService graphService, int batchNumber) throws RemoteException {
        String batchRequest = batchGenerator.generateBatch();
        long startTime = System.currentTimeMillis();
        String batchResult = graphService.executeBatch(batchRequest, clientID);
        long endTime = System.currentTimeMillis();
        logBatchResult(batchNumber, batchRequest, batchResult, endTime - startTime);
    }

    private void logBatchResult(int batchNumber, String batchRequest, String batchResult, long executionTime) {
        logger.log("ClientID: " + clientID + " batch #" + batchNumber + " report\n"
                + "---------------------------------------------------------------\n"
                + "Batch request:\n" + batchRequest
                + "\n---------------------------------------------------------------\n"
                + "Batch result:\n" + batchResult
                + "\n---------------------------------------------------------------\n"
                + "Execution time: " + executionTime + " ms\n\n\n");

    }
}