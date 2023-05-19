package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Registry.IGraphService;

public class GraphService extends UnicastRemoteObject implements IGraphService {
    private Graph graph;
    private Lock lock;
    private LoggerManger logger;

    public GraphService(int variant, String filePath) throws RemoteException {
        super();
        logger = new LoggerManger();
        logger.log("Graph Service Starting");
        initializeGraph(variant, filePath);
        lock = new ReentrantLock(true);
        logger.log("Graph Service Started and ready to receive requests");
    }

    @Override
    public String executeBatch(String batch, int clientID) throws RemoteException {
        long RequestStartTime = System.currentTimeMillis();
        String[] operations = batch.split("\n");
        ArrayList<Integer> results = new ArrayList<>();
        logger.log("Client " + clientID + " requested to execute a batch");

        // Acquire Lock
        lock.lock();
        logger.log("Client " + clientID + " Acquired Lock");

        long executionStartTime = System.currentTimeMillis();
        applyBatchOperations(operations, results);
        long executionEndTime = System.currentTimeMillis();

        // Release Lock
        lock.unlock();
        logger.log("Client " + clientID + " Released Lock");

        String result = results.stream().map(Object::toString).reduce("", (a, b) -> a + b + "\n");
        long RequestEndTime = System.currentTimeMillis();
        Thread loggingThread = new Thread(() -> {
            logClientRequest(clientID, batch, result, RequestEndTime - RequestStartTime,
                    executionEndTime - executionStartTime);
        });
        loggingThread.start();
        return result;
    }

    private void initializeGraph(int variant, String filePath) {
        graph = variant == 0 ? new BFSGraph() : new BiBFSGraph();
        try {
            BufferedReader inputReader = new BufferedReader(new FileReader(filePath));
            inputReader.lines().forEach(line -> {
                if (line.equals("S"))
                    return;
                String[] nodes = line.split("\\s");
                graph.addEdge(Integer.parseInt(nodes[0]), Integer.parseInt(nodes[1]));

            });
            inputReader.close();
            logger.log("Graph Initialized");
            logger.log("\nR\n");
        } catch (IOException e) {
            System.err.println("Error reading initial graph file");
            e.printStackTrace();
        }
    }

    private void applyBatchOperations (String[] operations, ArrayList<Integer> results) {
        for (String operation : operations) {
            if (operation.equals("F"))
                break;
            String[] operationParts = operation.split("\\s");
            int srcNode = Integer.parseInt(operationParts[1]);
            int destNode = Integer.parseInt(operationParts[2]);
            if (operationParts[0].equals("A"))
                graph.addEdge(srcNode, destNode);
            else if (operationParts[0].equals("D"))
                graph.removeEdge(srcNode, destNode);
            else if (operationParts[0].equals("Q"))
                results.add(graph.shortestPathLength(srcNode, destNode));
        }
    }

    private void applyBatchOperationsThreaded (String[] operations, ArrayList<Integer> results) {
        ArrayList<Thread> readThreads = new ArrayList<>();
        Map<Integer, Integer> readResults = new ConcurrentHashMap<>();
        for (int i = 0; i < operations.length; i++) {
            String operation = operations[i];
            if (operation.equals("F"))
                break;
            String[] operationParts = operation.split("\\s");
            int srcNode = Integer.parseInt(operationParts[1]);
            int destNode = Integer.parseInt(operationParts[2]);
            if (operationParts[0].equals("A")) {
                readThreads.forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                graph.addEdge(srcNode, destNode);
            }
            else if (operationParts[0].equals("D")) {
                readThreads.forEach(thread -> {
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                graph.removeEdge(srcNode, destNode);
            }
            else if (operationParts[0].equals("Q")) {
                int index = i;
                Thread readThread = new Thread(() -> {
                    readResults.put(index,graph.shortestPathLength(srcNode, destNode));
                });
                readThread.start();
                readThreads.add(readThread);
            }
        }
        readThreads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < operations.length; i++) {
            if (readResults.containsKey(i))
                results.add(readResults.get(i));
        }
    }

    private void logClientRequest(int clientID, String batch, String result, long requestTime, long executionTime) {
        logger.log("ClientID: " + clientID + " Request\n"
        + "---------------------------------------------------------------\n"
        + "Operations: \n" + batch
        + "\n---------------------------------------------------------------\n"
        + "Result: \n" + result
        + "\n---------------------------------------------------------------\n"
        + "Request Time: " + requestTime + " ms\nExecution Time: " + executionTime + " ms\n\n\n");
    }
}
