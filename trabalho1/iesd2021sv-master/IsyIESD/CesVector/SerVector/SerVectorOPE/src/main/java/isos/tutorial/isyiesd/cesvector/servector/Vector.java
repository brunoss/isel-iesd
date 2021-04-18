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
    private static VectorWriteOperation operations = null;
    private static long lastWriteTime = 0;
    private static int MaxWaitingTime = 3000;
	private static String currentClient = null;
    
    @Resource
    private WebServiceContext requestContext;
    
    public static int sum() {
    	long now = System.currentTimeMillis();
    	synchronized(lock) {
    		long elapsed = now - lastWriteTime;
    		if(elapsed > MaxWaitingTime) {
    			operations = null;
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
    
    public String getIp() {
    	MessageContext context = requestContext.getMessageContext();
		HttpExchange exchange = (HttpExchange)context.get(JAXWSProperties.HTTP_EXCHANGE);
	    InetSocketAddress address = exchange.getRemoteAddress();
	    String host = address.getAddress().toString() + address.getPort();
	    return host;
    }
    
    @Override
    public int read(int pos) 
    {
    	synchronized(lock) {
    		if(currentClient == null || currentClient.equals(getIp())) {
        		currentClient = getIp();
        		return vector.get(pos);
    		}
    		while(currentClient != null) {
    			try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    		currentClient = getIp();
    		return vector.get(pos);
    	}
    }

    @Override
    public void write(int pos, int n) 
    {
    	//Obtém a thread que atende o pedido Thread.currentThread().getId();
    	synchronized(lock) {
    		if(operations == null) {
    			operations = new VectorWriteOperation();
    			operations.SourcePosition = pos;
    			operations.SourceValue = n;
    		} else {
    			operations.DestinationPosition = pos;
    			operations.DestinationValue = n;
    			
    			vector.set(operations.SourcePosition, operations.SourceValue);
    			vector.set(operations.DestinationPosition, operations.DestinationValue);
    			operations = null;
    			currentClient = null;
    			lock.notifyAll();
    		}
    		lastWriteTime = System.currentTimeMillis();
    	}
    }
}
