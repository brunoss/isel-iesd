package org.mdeos.osgi.transaction;

public interface ILockNotifier {
	void NotifyAvailableLock(String token);
}
