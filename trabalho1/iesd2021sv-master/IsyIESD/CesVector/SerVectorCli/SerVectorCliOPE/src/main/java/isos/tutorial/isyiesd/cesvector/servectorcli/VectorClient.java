package isos.tutorial.isyiesd.cesvector.servectorcli;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import isos.tutorial.isyiesd.cesvector.servectorcli.Vector.IVector;
import isos.tutorial.isyiesd.cesvector.servectorcli.Vector.VectorService;

public class VectorClient {
	private static final BlockingQueue queue = new ArrayBlockingQueue<Runnable>(200);
	private static final Random r = new Random();
	private static final ThreadPoolExecutor threadPool = 
			new ThreadPoolExecutor(4, 16, 0L, TimeUnit.MILLISECONDS, queue);
	
	private static final Timer timer = new Timer();
	private static AtomicInteger nRequests = new AtomicInteger();
	
	static 
	{
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				int requests = nRequests.getAndSet(0);
				System.out.println(requests / 10);
			}
		}, 0, 10 * 1000);
	}
	
	  public static boolean createFile() {
	    try {
	      File myObj = new File("filename.txt");
	      System.out.println(myObj.getAbsolutePath());
	      return myObj.createNewFile();
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	      return false;
	    }
	  }
	  
	public static boolean deleteFile() {
      File myObj = new File("filename.txt");
      return myObj.delete();
	}
	  
	public static void fistExample() throws InterruptedException 
	{
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
	
	public static void singleClient() 
	{
		VectorService vectorService = new VectorService();
        IVector vectorClient = vectorService.getVectorPort();
        
        int nAttempts = 0;
		
		while(true) {
			if(!createFile()) {
				try {
					Thread.sleep(100);
					++nAttempts;
					if(nAttempts > 100) {
						deleteFile();
						nAttempts = 0;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			//lê o valor dum indice aleatório do array
			int srcIdx = r.nextInt(4);
			int srcAmount = vectorClient.read(srcIdx);
			nRequests.incrementAndGet();
			
			//subtrai uma quantia aleatória
			int value = r.nextInt(srcAmount / 2);
			vectorClient.write(srcIdx, srcAmount - value);
			nRequests.incrementAndGet();

			//lê o valor dum indice aleatório do array (diferente do primeiro)
			int dstIdx = r.nextInt(4);
			while(dstIdx == srcIdx) {
				dstIdx = r.nextInt(4);
			}
			int dstAmount = vectorClient.read(dstIdx);
			nRequests.incrementAndGet();
			
			//acrescenta a quantia previamente retirada.
			vectorClient.write(dstIdx, dstAmount + value);
			nRequests.incrementAndGet();
			deleteFile();
		}
	}
	
	public static void multipleClients() 
	{
		for(int i = 0; i < 16; ++i) 
		{
			threadPool.execute(() -> singleClient());
		}
	}
	
    public static void main(String[] args) throws InterruptedException 
    {
    	singleClient();
    }
}
