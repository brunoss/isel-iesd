import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

public class VectorService extends UnicastRemoteObject implements  IVector{
    private static List<Integer> vector = Arrays.asList(300, 234, 56, 789);

    protected VectorService() throws RemoteException {
    }

    @Override
    public int read(String token, int pos) throws RemoteException {
        return vector.get(pos);
    }

    @Override
    public void write(String token, int pos, int value) throws RemoteException {
        vector.set(pos, value);
    }
}
