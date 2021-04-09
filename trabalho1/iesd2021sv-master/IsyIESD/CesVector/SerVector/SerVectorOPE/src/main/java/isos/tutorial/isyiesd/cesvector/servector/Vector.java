package isos.tutorial.isyiesd.cesvector.servector;

import javax.jws.WebService;
import java.util.Arrays;
import java.util.List;

@WebService(endpointInterface = "isos.tutorial.isyiesd.cesvector.servector.IVector")
public class Vector implements IVector {
    private static List<Integer> vector = Arrays.asList(300, 234, 56, 789);
    private static Object lock = new Object();
    private static int sum = sum();
    private static int nRequests = 0;
    
    public static int sum() {
    	synchronized(lock) {
    		while(nRequests % 2 != 0) {
    			try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
        	int sum = 0;
        	for(int i = 0; i < vector.size(); ++i) {
        		sum += vector.get(i);
        	}
        	return sum;
    	}
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
    	//Obtém a thread que atende o pedido Thread.currentThread().getId();
    	synchronized(lock) {
    		vector.set(pos, n);
    		nRequests++;
    		lock.notifyAll();
    	}
    }
}
