package isos.tutorial.isyiesd.cesvector.servector;

import javax.xml.ws.Endpoint;

/**
 * Hello world!
 */
public class SiteServer {

    public static void main(String[] args) {
    	int sum = Vector.sum(2000);
    	System.out.println(sum + "");
    	
        Endpoint ep = Endpoint.create(new Vector());
        System.out.println("Starting SiteServer...");
        ep.publish("http://localhost:2058/Vector");
        
        Thread thread = new Thread(() -> {
        	long elapsed = -1;
        	long time = System.currentTimeMillis();
        	
        	while(true) {
        		int currentSum = Vector.sum(2000);
        		if(sum != currentSum) {
                	System.out.println("INVARIANTE NÃO CUMPRIDA");
                	System.out.println(currentSum + "");
                	break;
        		}else {
        			if(elapsed < 0 || elapsed > 10 * 1000) {
                    	time = System.currentTimeMillis();
                    	System.out.println("Invariante cumprida");
        			}
    				elapsed = System.currentTimeMillis() - time;
        		}
        		
        		try {
        			//Só para poupar energia e não estar o tempo todo a verificar a invariante.
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        });
        thread.start();
    }

}
