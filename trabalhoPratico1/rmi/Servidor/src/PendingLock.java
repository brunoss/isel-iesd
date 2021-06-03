import java.util.Collection;

public class PendingLock {
    public ILockNotifier LockNotifier;
    public Collection<OperationIdentifier> OperationIdentifier;

    public boolean isConflictingWith(OperationIdentifier operation) {
        for(OperationIdentifier op1 : OperationIdentifier) {
            if(op1.isConflictingWith(operation)) {
                return true;
            }
        }
        return false;
    }
}
