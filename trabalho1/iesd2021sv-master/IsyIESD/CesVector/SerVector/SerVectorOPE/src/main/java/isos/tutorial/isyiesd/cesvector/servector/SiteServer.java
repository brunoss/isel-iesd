package isos.tutorial.isyiesd.cesvector.servector;

import javax.xml.ws.Endpoint;

/**
 * Hello world!
 */
public class SiteServer {

    public static void main(String[] args) {
        Endpoint ep = Endpoint.create(new Vector());
        System.out.println("Starting SiteServer...");
        ep.publish("http://localhost:2058/Vector");
    }

}
