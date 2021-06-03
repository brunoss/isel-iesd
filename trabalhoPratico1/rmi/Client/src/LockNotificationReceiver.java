import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LockNotificationReceiver extends UnicastRemoteObject implements ILockNotifier
{
    protected LockNotificationReceiver() throws RemoteException {
    }

    @Override
    public void NotifyAvailableLock(String token) throws RemoteException {
        System.out.println(token);
    }
}
