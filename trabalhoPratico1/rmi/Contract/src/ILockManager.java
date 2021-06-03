import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface ILockManager extends Remote {
    String getLocks(String token, Collection<OperationIdentifier> operations, ILockNotifier notifier) throws RemoteException;
    void unlock(String token) throws  RemoteException;
}
