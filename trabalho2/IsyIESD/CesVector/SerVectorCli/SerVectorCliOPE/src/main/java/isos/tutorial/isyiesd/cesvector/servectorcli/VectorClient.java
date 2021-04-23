package isos.tutorial.isyiesd.cesvector.servectorcli;

import isos.tutorial.isyiesd.cesvector.servectorcli.Vector.IVector;
import isos.tutorial.isyiesd.cesvector.servectorcli.Vector.VectorService;

/**
 * Hello world!
 */
public class VectorClient {

    public static void main(String[] args) throws InterruptedException {

        VectorService service = new VectorService();
        IVector port = service.getVectorPort();

        int v, res;
        int x = 100;

        v = port.read(0);
        res = v - x;
        Thread.sleep(1);

        port.write(0, res);
        Thread.sleep(10);

        v = port.read(2);
        res = v + x;
        Thread.sleep(10);

        port.write(2, res);

    }
}
