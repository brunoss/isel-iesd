package org.mdeos.osgi.transaction;

import java.util.Collection;

public interface ITransactionManager {
	String getToken(Collection<OperationIdentifier> operations);

	Collection<OperationIdentifier> identifyTransaction(String token);
	
	void commit(String token);
}
