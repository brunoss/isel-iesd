import java.io.Serializable;

public class OperationIdentifier implements Serializable {
    public String Server;
    public Operation Operation;
    public int Position;

    @Override
    public boolean equals(Object obj) {
        return equals((OperationIdentifier) obj);
    }

    public boolean isConflictingWith(OperationIdentifier other) {
        boolean result = Position == other.Position;
        result = result && Server.equals(other.Server);
        result = result && (Operation.equals(Operation.Write) || other.Operation.equals(Operation.Write));
        return result;
    }

    public boolean equals(OperationIdentifier other) {
        boolean result = Server.equals(other.Server);
        //result = result && Operation == other.Operation;
        result = result && Position == other.Position;
        return result;
    }

    @Override
    public int hashCode() {
        int hash = Server.hashCode();
        //hash = hash ^ Operation.hashCode();
        return hash;
    }
}
