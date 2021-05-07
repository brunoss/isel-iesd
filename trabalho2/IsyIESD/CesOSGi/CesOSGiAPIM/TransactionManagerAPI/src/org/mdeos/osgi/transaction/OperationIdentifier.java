package org.mdeos.osgi.transaction;

public class OperationIdentifier {
	public String Server;
	public Operation Operation;
	
	@Override
	public boolean equals(Object obj) {
		return equals((OperationIdentifier) obj);
	}
	
	public boolean equals(OperationIdentifier other) {
		boolean result = Server.equals(other.Server);
		//result = result && Operation == other.Operation;
		return result;
	}
	
	@Override
	public int hashCode() {
		int hash = Server.hashCode();
		//hash = hash ^ Operation.hashCode();
		return hash;
	}
}
