import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class Servidor{


    public static void main(String[] args) {
        try {
            Properties props = System.getProperties();
            props.put("java.rmi.server.hostname", "localhost");

            Somador svc = new Somador(0);
            Registry registry = LocateRegistry.createRegistry(8001);
            registry.rebind("RemServer", svc); //regista skeleton com nome lógico

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
