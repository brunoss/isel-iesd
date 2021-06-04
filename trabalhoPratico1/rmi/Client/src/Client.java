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
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

public class Client {
    private static ILockManager lockService;
    private static ITransactionManager transactionManager;
    private static final ArrayList<IVector> vectorServices = new ArrayList<>();

    private static final String LockManagerPath = "LockManager";
    private static final String TransactionManagerPath = "TransactionManager";
    private static final String VectorServicePath = "VectorService";
    private static final int NServers = 4;
    private static int CallbackPort = 10000;

    static final Random r = new Random();

    private static int getRandom(int n, boolean isServer) {
        if(isServer && vectorServices.size() == 1){
            return 0;
        }
        int max = isServer ? vectorServices.size() : 4;
        while (true){
            int random = r.nextInt(max);
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

        //lockService.getLocks("1", operations, new LockNotificationReceiver());
    }

    private static void ReadAndWriteValues() throws RemoteException {
        int random = r.nextInt(vectorServices.size());
        IVector vector1 = vectorServices.get(random);
        String vector1Name = VectorServicePath + random;

        random = getRandom(random, true);
        IVector vector2 = vectorServices.get(random);
        String vector2Name = VectorServicePath + random;

        Collection<OperationIdentifier> operations = new ArrayList<OperationIdentifier>();

        OperationIdentifier read1 = new OperationIdentifier();
        read1.Server = vector1Name;
        read1.Operation = Operation.Read;
        operations.add(read1);

        OperationIdentifier write1 = new OperationIdentifier();
        write1.Server = vector1Name;
        write1.Operation = Operation.Write;
        operations.add(write1);

        OperationIdentifier read2 = new OperationIdentifier();
        read2.Server = vector2Name;
        read2.Operation = Operation.Read;
        operations.add(read2);

        OperationIdentifier write2 = new OperationIdentifier();
        write2.Server = vector2Name;
        write2.Operation = Operation.Write;
        operations.add(write2);

        while(true) {
            random = r.nextInt(4);
            read1.Position = random;
            write1.Position = random;
            read2.Position = getRandom(random, false);
            write2.Position = read2.Position;
            Semaphore canContinue = new Semaphore(0);

            String token = transactionManager.getToken(operations);
            lockService.getLocks(token, operations, new LockNotificationReceiver(CallbackPort){

                @Override
                public void NotifyAvailableLock(String token) throws RemoteException {
                    int value = vector1.read(token, (read1.Position));
                    int ammount = r.nextInt(value / 2);

                    vector1.write(token, write1.Position, value - ammount);

                    value = vector2.read(token, read2.Position);
                    vector2.write(token, write2.Position, value + ammount);

                    transactionManager.commit(token);
                    lockService.unlock(token);
                    canContinue.release();
                }
            });
            try {
                canContinue.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if(args.length > 0){
            CallbackPort = Integer.parseInt(args[0]);
        }

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8001);

            lockService = (ILockManager) registry.lookup(LockManagerPath);
            transactionManager = (ITransactionManager) registry.lookup(TransactionManagerPath);
            for(int i = 0; i < NServers; ++i){
                vectorServices.add((IVector) registry.lookup(VectorServicePath + i));
            }

            ReadAndWriteValues();

            System.in.read();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Client unhandled exception: " + ex.toString());
        }

    }
}
