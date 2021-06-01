import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LockManager extends UnicastRemoteObject implements ILockManager {
    private static final Map<String, Collection<OperationIdentifier>> lockMap = new HashMap<String, Collection<OperationIdentifier>>();
    private static final Map<String, PendingLock> pendingLocksMap = new HashMap<String, PendingLock>();

    protected LockManager() throws RemoteException {
    }

    public static boolean createFile() {
        try {
            File myObj = new File("filename.txt");
            System.out.println(myObj.getAbsolutePath());
            return myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFile() {
        File myObj = new File("filename.txt");
        return myObj.delete();
    }


    @Override
    public String getLocks(String token, Collection<OperationIdentifier> operations, ILockNotifier notifier) {
        String guid = java.util.UUID.randomUUID().toString();
        if(createFile()) {
            lockMap.put(guid, operations);
            try {
                if(notifier != null){
                    notifier.NotifyAvailableLock(guid);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return guid;
        }else {
            PendingLock lock = new PendingLock();
            lock.LockNotifier = notifier;
            lock.OperationIdentifier = operations;
            pendingLocksMap.put(guid, lock);
            return null;
        }
    }

    @Override
    public void unlock(String token) {
        if(lockMap.containsKey(token)) {
            lockMap.remove(token);
        }

        if(pendingLocksMap.size() > 0) {
            for(Map.Entry<String, PendingLock> lock : pendingLocksMap.entrySet()) {
                boolean hasConflict = false;
                Stream<OperationIdentifier> stream = lockMap.values().stream().flatMap(Collection::stream);
                Collection<OperationIdentifier> allOperations = stream.collect(Collectors.toList());

                for(OperationIdentifier operation : allOperations) {
                    if(lock.getValue().isConflictingWith(operation)) {
                        hasConflict = true;
                        break;
                    }
                }

                if(!hasConflict) {
                    lockMap.put(lock.getKey(), allOperations);
                    try {
                        if(lock.getValue().LockNotifier != null){
                            lock.getValue().LockNotifier.NotifyAvailableLock(lock.getKey());
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
