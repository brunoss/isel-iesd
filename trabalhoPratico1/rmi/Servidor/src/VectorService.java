import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class VectorService extends UnicastRemoteObject implements  IVector{
    private static List<Integer> vector = Arrays.asList(300, 234, 56, 789);
    private static Map<String, ArrayList<VectorWriteOperation>> operationMap = new HashMap<>();

    protected VectorService() throws RemoteException {
    }

    @Override
    public int read(String token, int pos) throws RemoteException {
        return vector.get(pos);
    }

    @Override
    public void write(String token, int pos, int value) throws RemoteException {
        ArrayList<VectorWriteOperation> operationList = null;
        if(!operationMap.containsKey(token)){
            operationList = new ArrayList<VectorWriteOperation>();
            operationMap.put(token, operationList);
        }else{
            operationList = operationMap.get(token);
        }
        VectorWriteOperation operation = new VectorWriteOperation();
        operation.Position = pos;
        operation.Value = value;
        operationList.add(operation);
    }

    @Override
    public int sum() {
        int sum = 0;
        for(int i = 0; i < vector.size(); ++i) {
            sum += vector.get(i);
        }
        return sum;
    }

    @Override
    public boolean prepare(String token) throws RemoteException {
        return true;
    }

    @Override
    public void commit(String token) throws RemoteException {
        ArrayList<VectorWriteOperation> operationList = null;
        if(!operationMap.containsKey(token)){
            return;
        }else{
            operationList = operationMap.get(token);
        }

        for(VectorWriteOperation operation : operationList){
            vector.set(operation.Position, operation.Value);
        }

        operationMap.remove(token);
    }

    @Override
    public void rollback(String token) throws RemoteException {
        if(operationMap.containsKey(token)){
            operationMap.remove(token);
        }
    }
}
