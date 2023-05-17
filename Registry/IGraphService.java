package Registry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGraphService extends Remote {
    public String executeBatch (String batch, int clientID) throws RemoteException;
}
