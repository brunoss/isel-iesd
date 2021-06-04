import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IVector extends Remote {
    int read(String token, int pos) throws RemoteException;
    void write(String token, int pos, int value) throws RemoteException;
    int sum() throws RemoteException;

    boolean prepare(String token) throws RemoteException;
    void commit(String token) throws RemoteException;
    void rollback(String token) throws RemoteException;
}
