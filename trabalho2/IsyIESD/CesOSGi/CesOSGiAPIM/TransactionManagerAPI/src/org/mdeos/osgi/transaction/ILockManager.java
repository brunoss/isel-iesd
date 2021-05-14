package org.mdeos.osgi.transaction;

import java.util.Collection;

public interface ILockManager {
	String getLocks(String token, Collection<OperationIdentifier> operations, ILockNotifier notifier);
	void unlock(String token);
}
