import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class LockNotificationReceiver extends UnicastRemoteObject implements ILockNotifier
{
    protected LockNotificationReceiver(int port) throws RemoteException {
        super(port);
    }

}
