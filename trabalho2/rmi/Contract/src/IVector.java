import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IVector extends Remote {
    int read(String token, int pos) throws RemoteException;
    void write(String token, int pos, int value) throws RemoteException;
}
