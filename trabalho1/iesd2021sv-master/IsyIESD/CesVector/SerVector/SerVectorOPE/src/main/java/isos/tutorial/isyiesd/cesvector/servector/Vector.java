package isos.tutorial.isyiesd.cesvector.servector;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.spi.http.HttpExchange;
import com.sun.xml.ws.developer.JAXWSProperties;
import sun.net.httpserver.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@WebService(endpointInterface = "isos.tutorial.isyiesd.cesvector.servector.IVector")
public class Vector implements IVector {
    private static List<Integer> vector = Arrays.asList(300, 234, 56, 789);
    private static Object lock = new Object();
    private static int nRequests = 0;
    private static VectorWriteOperation operations = null;
    private static long lastWriteTime = 0;
    private static int MaxWaitingTime = 5000;
	private static Random rand = new Random();
    
    @Resource
    private WebServiceContext wsc;
    
    public static int sum(int waitTime) {
    	long now = System.currentTimeMillis();
    	synchronized(lock) {
    		/*
    		while(nRequests % 2 != 0) {
    			try {
					lock.wait(waitTime);
					long elapsed = System.currentTimeMillis() - now;
					if(elapsed > waitTime) {
						return -1;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    		*/
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
    
    @Override
    public int read(int pos) 
    {
    	synchronized(lock) {
    		return vector.get(pos);
    	}
    }
    
    private String getIP(){
    	return rand.nextInt(2) + "";
    }

    @Override
    public void write(int pos, int n) 
    {
    	/*
    	MessageContext context = wsc.getMessageContext();
    	HttpExchange req = (HttpExchange)context.get(JAXWSProperties.HTTP_EXCHANGE); 
        String host = req.getRemoteAddress().getHostName(); 
        */

        String host = getIP();
        
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
    		}
    		lastWriteTime = System.currentTimeMillis();
    		lock.notifyAll();
    	}
    }
}
