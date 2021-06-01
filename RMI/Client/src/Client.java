import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8001);
            SumInterface svc = (SumInterface) registry.lookup("RemServer");
        for(int i=0; i<100;i++){
            int r = svc.sum(3, i*12);

            System.out.println("Result: " + r);
        }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Client unhandled exception: " + ex.toString());
        }

    }
}
