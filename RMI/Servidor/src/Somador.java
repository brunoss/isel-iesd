import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Somador extends UnicastRemoteObject implements SumInterface{
    protected Somador(int port) throws RemoteException {
        super(port);
    }

    @Override
    public int sum(int i, int i1) throws RemoteException {
        return i+i1;
    }
}
