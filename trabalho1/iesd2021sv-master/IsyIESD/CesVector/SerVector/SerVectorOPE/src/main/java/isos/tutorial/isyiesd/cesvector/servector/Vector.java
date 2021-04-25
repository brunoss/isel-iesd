package isos.tutorial.isyiesd.cesvector.servector;

import com.sun.net.httpserver.HttpExchange;
import com.sun.xml.ws.developer.JAXWSProperties;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

@WebService(endpointInterface = "isos.tutorial.isyiesd.cesvector.servector.IVector")
public class Vector implements IVector {
    private static List<Integer> vector = Arrays.asList(300, 234, 56, 789);
    private static Object lock = new Object();
    private static int nRequests = 0;
    
    @Resource
    private WebServiceContext requestContext;
    
    public static int sum() {
    	synchronized(lock) {
    		while(nRequests % 2 != 0) {
    			try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    		return getSum();
    	}
    }
    
    public static int getSum() {
    	int sum = 0;
    	for(int i = 0; i < vector.size(); ++i) {
    		sum += vector.get(i);
    	}
    	return sum;
    }
    
    @Override
    public int read(int pos) 
    {
    	synchronized(lock) {
    		return vector.get(pos);
    	}
    }

    @Override
    public void write(int pos, int n) 
    {
    	synchronized(lock) {
			vector.set(pos, n);
			++nRequests;
			lock.notifyAll();
    	}
    }
}
