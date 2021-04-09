package isos.tutorial.isyiesd.cesvector.servector;

import javax.xml.ws.Endpoint;

/**
 * Hello world!
 */
public class SiteServer {

    public static void main(String[] args) {
    	int sum = Vector.sum();
    	System.out.print(sum + "");
    	
        Endpoint ep = Endpoint.create(new Vector());
        System.out.println("Starting SiteServer...");
        ep.publish("http://localhost:2058/Vector");
        
        Thread thread = new Thread(() -> {
        	while(true) {
        		int thisSum = Vector.sum();
        		if(sum != thisSum) {
                	System.out.println("Invariante não cumprida");
                	System.out.println(thisSum + "");
                	break;
        		}else {
                	System.out.println("Invariante cumprida");
                	try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	}
        	
        });
        thread.start();
    }

}
