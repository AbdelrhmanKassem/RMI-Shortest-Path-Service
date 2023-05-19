package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {
        GraphService graphService = new GraphService(1, "initialGraph.txt");
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("GraphService", graphService);
    }
}
