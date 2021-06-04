import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Properties;

public class Servidor{
    private static final int NVectorServices = 4;

    private static ArrayList<UnicastRemoteObject> CreateServices() throws RemoteException {
        ArrayList<VectorService> vectorServiceList = new ArrayList<>();
        for(int i = 0; i < NVectorServices; ++i){
            vectorServiceList.add(new VectorService(i));
        }

        ArrayList<UnicastRemoteObject> services = new ArrayList<UnicastRemoteObject>();
        services.addAll(vectorServiceList);
        services.add(new LockManager());
        services.add(new TransactionManager(vectorServiceList));

        return  services;
    }

    public static void main(String[] args) {
        try {
            Properties props = System.getProperties();
            props.put("java.rmi.server.hostname", "localhost");

            Registry registry = LocateRegistry.createRegistry(8001);
            ArrayList<UnicastRemoteObject> services = CreateServices();
            for(int i = 0; i < services.size(); ++i){
                String serviceName = services.get(i).getClass().getName();
                if(serviceName.contains("Vector")){
                    serviceName += i;
                }
                registry.rebind(serviceName, services.get(i));
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
