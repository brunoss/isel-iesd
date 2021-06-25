import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// UnicastRemoteObject - Used for exporting a remote object with JRMP and obtaining a stub that communicates to the remote object.
public class Somador extends UnicastRemoteObject implements SumInterface {
    protected Somador(int port) throws RemoteException {
        // Creates and exports a new UnicastRemoteObject object using the particular supplied port.
        super(port);
    }

    @Override
    public int sum(int i, int i1) throws RemoteException {
        return i+i1;
    }
}
