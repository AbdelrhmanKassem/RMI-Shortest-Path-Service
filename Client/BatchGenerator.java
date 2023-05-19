package Client;

public class BatchGenerator {
    private int numberOfOperations;
    private int writePercentage;
    private int graphNodeCount;

    public BatchGenerator(int numberOfOperations, int graphNodeCount, int writePercentage) {
        this.numberOfOperations = numberOfOperations;
        this.graphNodeCount = graphNodeCount;
        this.writePercentage = writePercentage;
    }

    public String generateBatch() {
        StringBuilder batchBuilder = new StringBuilder();
        for (int i = 0; i < numberOfOperations; i++) {
            int operationType = (int) (Math.random() * 100);
            int node1 = (int) (Math.random() * graphNodeCount) + 1;
            int node2 = (int) (Math.random() * graphNodeCount) + 1;
            if (operationType < writePercentage) {
                // Write Operation
                char opCode = Math.random() < 0.5 ? 'A' : 'D';
                batchBuilder.append(opCode + " " + node1 + " " + node2 + "\n");
            } else {
                // Read Operation
                batchBuilder.append("Q " + node1 + " " + node2 + "\n");
            }
        }
        batchBuilder.append('F');
        return batchBuilder.toString();
    }
}
