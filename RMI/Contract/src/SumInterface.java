import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SumInterface extends Remote {

    int sum(int x, int y) throws RemoteException;
}

class Request implements Serializable {



}