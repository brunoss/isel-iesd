import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class Servidor{

    private static UnicastRemoteObject[] CreateServices() throws RemoteException {
        UnicastRemoteObject[] services = new UnicastRemoteObject[]{
            new LockManager(),
            new TransactionManager(),
            new VectorService()
        };
        return  services;
    }

    public static void main(String[] args) {
        try {
            Properties props = System.getProperties();
            props.put("java.rmi.server.hostname", "localhost");

            Registry registry = LocateRegistry.createRegistry(8001);
            UnicastRemoteObject[] services = CreateServices();
            for(int i = 0; i < services.length; ++i){
                registry.rebind(services[i].getClass().getName(), services[i]);
            }

            System.err.println(props.get("java.rmi.server.hostname"));

            System.out.println("Server ready: Press any key to finish server");
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String line = scanner.nextLine(); System.exit(0);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Server unhandled exception: " + ex.toString());
        }


    }

}
