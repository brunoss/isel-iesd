import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface ITransactionManager extends Remote {
    String getToken(Collection<OperationIdentifier> operations) throws RemoteException;

    Collection<OperationIdentifier> identifyTransaction(String token) throws RemoteException;

    boolean commit(String token) throws RemoteException;
}