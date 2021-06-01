import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

public class TransactionManager extends UnicastRemoteObject implements ITransactionManager {
    protected TransactionManager() throws RemoteException {
    }

    @Override
    public String getToken(Collection<OperationIdentifier> operations) throws RemoteException {
        return null;
    }

    @Override
    public Collection<OperationIdentifier> identifyTransaction(String token) throws RemoteException {
        return null;
    }

    @Override
    public void commit(String token) throws RemoteException {

    }
}
