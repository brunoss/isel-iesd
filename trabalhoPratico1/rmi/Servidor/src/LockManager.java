import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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

    @Override
    public boolean getLocks(String token, Collection<OperationIdentifier> operations, ILockNotifier notifier) {
        boolean lockCreated = false;
        synchronized (lockMap){
            if(canCreateLock(operations)) {
                lockMap.put(token, operations);
                lockCreated = true;
            }
            else
            {
                PendingLock lock = new PendingLock();
                lock.LockNotifier = notifier;
                lock.OperationIdentifier = operations;
                pendingLocksMap.put(token, lock);
                return false;
            }
        }
        if(lockCreated){
            try {
                if(notifier != null){
                    notifier.NotifyAvailableLock(token);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return lockCreated;
    }

    private boolean canCreateLock(Collection<OperationIdentifier> operations){
        PendingLock lock = new PendingLock();
        lock.OperationIdentifier = operations;
        return canCreateLock(lock);
    }

    private boolean canCreateLock(PendingLock lock){
        boolean canCreateLock = !lockMap
                .values()
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(o -> lock.isConflictingWith(o));

        return canCreateLock;
    }

    @Override
    public void unlock(String token) {
        ArrayList<Map.Entry<String, PendingLock>> notifications = new ArrayList<>();

        synchronized (lockMap){
            if(lockMap.containsKey(token)) {
                lockMap.remove(token);
            }

            if(pendingLocksMap.size() > 0) {
                for(Map.Entry<String, PendingLock> lock : pendingLocksMap.entrySet()) {
                    Stream<OperationIdentifier> stream = lockMap.values().stream().flatMap(Collection::stream);
                    Collection<OperationIdentifier> allOperations = stream.collect(Collectors.toList());

                    if(canCreateLock(lock.getValue())) {
                        lockMap.put(lock.getKey(), allOperations);
                        pendingLocksMap.remove(lock.getKey());
                        notifications.add(lock);
                    }
                }
            }
        }

        for(Map.Entry<String, PendingLock> current : notifications){
            try {
                current.getValue().LockNotifier.NotifyAvailableLock(current.getKey());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
