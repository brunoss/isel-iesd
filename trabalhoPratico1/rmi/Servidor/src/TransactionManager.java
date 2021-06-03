import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionManager extends UnicastRemoteObject implements ITransactionManager {
    private static final Map<String, Collection<OperationIdentifier>> lockMap = new HashMap<String, Collection<OperationIdentifier>>();
    private static final ArrayList<IVector> vectors = new ArrayList<>();
    private static int Sum;

    protected TransactionManager() throws RemoteException {
        Sum = sumVectors();
    }

    private int sumVectors(){
        int sum = 0;
        for(IVector vector : vectors){
            sum += vector.sum();
        }
        return sum;
    }

    @Override
    public String getToken(Collection<OperationIdentifier> operations) throws RemoteException {
        String guid = java.util.UUID.randomUUID().toString();
        lockMap.put(guid, operations);
        return guid;
    }

    @Override
    public Collection<OperationIdentifier> identifyTransaction(String token) throws RemoteException {
        return null;
    }

    @Override
    public boolean commit(String token) throws RemoteException {
        if(!lockMap.containsKey(token)){
            return false;
        }

        Collection<OperationIdentifier> operations = lockMap.get(token);
        lockMap.remove(token);

        Registry registry = LocateRegistry.getRegistry("localhost", 8001);
        Map<String, List<OperationIdentifier>> serverOperations = operations
                .stream()
                .collect(Collectors.groupingBy(o -> o.Server));
        boolean canCommit = true;

        for(Map.Entry<String, List<OperationIdentifier>> entry : serverOperations.entrySet()){
            try {
                IVector vector = (IVector) registry.lookup(entry.getKey());
                if(!vector.prepare(token))
                {
                    canCommit = false;
                    break;
                }
            } catch (Exception e) {
                canCommit = false;
                e.printStackTrace();
            }
        }

        for(Map.Entry<String, List<OperationIdentifier>> entry : serverOperations.entrySet()){
            try {
                IVector vector = (IVector) registry.lookup(entry.getKey());
                if(canCommit){
                    vector.commit(token);
                }else{
                    vector.rollback(token);
                }
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }

        if(Sum != sumVectors()){
            System.out.println("Deu Barraca!!!!");
        }

        return canCommit;
    }
}
