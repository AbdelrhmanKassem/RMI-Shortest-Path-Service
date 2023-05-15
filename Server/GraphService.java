package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Registry.IGraphService;

public class GraphService extends UnicastRemoteObject implements IGraphService {

    public GraphService() throws RemoteException {
		super();
	}

    @Override
    public String executeBatch(String batch) throws RemoteException {
        // TODO Execute batch of operations
        return "This is the result";
    }
    
}
