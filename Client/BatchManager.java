package Client;

public class BatchManager {
    private int clientID;
    private int numberOfOperations;
    private int writePercentage;
    private int graphNodeCount;
    private String batchRequest;

    public BatchManager(int clientID, int numberOfOperations, int graphNodeCount, int writePercentage) {
        this.clientID = clientID;
        this.numberOfOperations = numberOfOperations;
        this.graphNodeCount = graphNodeCount;
        this.writePercentage = writePercentage;
        this.batchRequest = "";
    }

    public String generateBatch() {
        if (batchRequest.length() > 0)
            return batchRequest;
        StringBuilder batchBuilder = new StringBuilder();
        for (int i = 0; i < numberOfOperations; i++) {
            int operationType = (int) (Math.random() * 100);
            if (operationType < writePercentage) {
                // Write Operation
                int node1 = (int) (Math.random() * graphNodeCount) + 1;
                int node2 = (int) (Math.random() * graphNodeCount) + 1;
                char opCode = Math.random() < 0.5 ? 'A' : 'D';
                batchBuilder.append(opCode + " " + node1 + " " + node2 + "\n");
            } else {
                // Read Operation
                int node1 = (int) (Math.random() * graphNodeCount);
                int node2 = (int) (Math.random() * graphNodeCount);
                batchBuilder.append("Q " + node1 + " " + node2 + "\n");
            }
        }
        batchBuilder.append('F');
        batchRequest = batchBuilder.toString();
        return batchRequest;
    }

    public void logBatchResult(String batchResult, long executionTime) {
        System.out.println("ClientID: " + clientID + " Batch: " + batchRequest);
        System.out.println(
                "ClientID: " + clientID + " Batch result: " + batchResult + " in :" + executionTime + " nanoseconds");
    }

}
