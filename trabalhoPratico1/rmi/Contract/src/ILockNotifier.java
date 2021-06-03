import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILockNotifier extends Remote {
    void NotifyAvailableLock(String token) throws RemoteException;
}
