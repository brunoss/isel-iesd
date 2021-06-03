import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Client {
    private static ILockManager lockService;
    private static ITransactionManager transactionManager;
    private static final ArrayList<IVector> vectorServices = new ArrayList<>();
    static final Random r = new Random();

    private static int getRandom(int n) {
        if(vectorServices.size() == 1){
            return 0;
        }
        while (true){
            int random = r.nextInt(vectorServices.size());
            if(random != n) {
                return random;
            }
        }
    }

    static void testLockManager() throws RemoteException {
        Collection<OperationIdentifier> operations = new ArrayList<OperationIdentifier>();
        OperationIdentifier writeOperation = new OperationIdentifier();
        writeOperation.Position = 0;
        writeOperation.Server = "localhost";
        writeOperation.Operation = Operation.Write;
        operations.add(writeOperation);

        lockService.getLocks("1", operations, new LockNotificationReceiver());
    }

    private static void ReadAndWriteValues() throws RemoteException {
        int random = r.nextInt(vectorServices.size());
        IVector vector1 = vectorServices.get(random);
        random = getRandom(random);
        IVector vector2 = vectorServices.get(random);

        Collection<OperationIdentifier> operations = new ArrayList<OperationIdentifier>();

        OperationIdentifier read1 = new OperationIdentifier();
        read1.Server = vector1.toString();
        read1.Operation = Operation.Read;
        operations.add(read1);

        OperationIdentifier write1 = new OperationIdentifier();
        write1.Server = vector1.toString();
        write1.Operation = Operation.Write;
        operations.add(write1);

        OperationIdentifier read2 = new OperationIdentifier();
        read2.Server = vector2.toString();
        read2.Operation = Operation.Read;
        operations.add(read2);

        OperationIdentifier write2 = new OperationIdentifier();
        write2.Server = vector2.toString();
        write2.Operation = Operation.Write;
        operations.add(write2);

        while(true) {
            String token = transactionManager.getToken(operations);
            lockService.getLocks(token, operations, new LockNotificationReceiver());

            random = r.nextInt(4);
            int value = vector1.read(token, random);
            int ammount = r.nextInt(value / 2);

            random = getRandom(random);
            vector1.write(token, random, value - ammount);

            random = r.nextInt(4);
            value = vector2.read(token, random);
            random = getRandom(random);
            vector2.write(token, random, value + ammount);

            transactionManager.commit(token);
            lockService.unlock(token);
        }
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8001);

            registry.rebind("NotificationReceiver", new LockNotificationReceiver());

            lockService = (ILockManager) registry.lookup("LockManager");
            transactionManager = (ITransactionManager) registry.lookup("TransactionManager");
            vectorServices.add((IVector) registry.lookup("VectorService"));

            testLockManager();

            System.in.read();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Client unhandled exception: " + ex.toString());
        }

    }
}
