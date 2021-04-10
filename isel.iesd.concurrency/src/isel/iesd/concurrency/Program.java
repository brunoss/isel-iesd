package isel.iesd.concurrency;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Program {

	public static void joinThreads(ArrayList<Thread> threads) {
		for(int i = 0; i < threads.size(); ++i) {
			Thread thread = threads.get(i);
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static ArrayList<Thread> startThreads(int nThreads, Runnable action, boolean join) {
		ArrayList<Thread> list = new ArrayList<Thread>();

		//Come�ar todas as threads "ao mesmo tempo"
		for(int i = 0; i < nThreads; ++i) {
			Thread thread = new Thread(action);
			list.add(thread);
			thread.start();
		}

		try {
			//D� oportunidade �s threads criadas para executarem alguma parte do seu c�digo
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Depois que as threads tiveram todas oportunidade de arrancar ent�o esperamos que elas acabem
		if(join) {
			joinThreads(list);
		}
		return list;
	}
	
	public static ArrayList<Thread> startThreads(int nThreads, Runnable action) {
		return startThreads(nThreads, action, true);
	}
	
	public static void deadLock() {
		Lock lock = new ReentrantLock(false);
		Lock lock2 = new ReentrantLock(false);
		
		Counter count = new Counter();
		int iterations = 100;
		
		ArrayList<Thread> threads = startThreads(1, () ->{
			for(int i = 0; i < iterations; ++ i) {

				//1. adquire lock1
				lock.lock();
				System.out.println("Thread1 adquiriu o lock1");
				try {
					Thread.sleep(50);
					
					//3. tenta adquirir o lock 2 e fica bloqueada
					lock2.lock();
					System.out.println("Thread1 adquiriu o lock2");
					count.Count++;
				} catch (InterruptedException e) {
					System.out.println("T1 has raised an exception");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					lock2.unlock();
					lock.unlock();
				}
			}
			System.out.println("Thread1 finnished");
		}, false);
		
		ArrayList<Thread> threads2 = startThreads(1, () ->{
			for(int i = 0; i < iterations; ++ i) {
				//2. adquiriu o lock
				lock2.lock();
				System.out.println("Thread2 adquiriu o lock2");
				try {
					Thread.sleep(100);
					//4. deadlock
					lock.lock();
					System.out.println("Thread2 adquiriu o lock1");
					count.Count++;
				} catch (InterruptedException e) {
					System.out.println("T2 has raised an exception");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					lock.unlock();
					lock2.unlock();
				}
			}
			System.out.println("Thread2 finnished");
		}, false);
		
		threads.addAll(threads2);
		joinThreads(threads);
	}
	
	public static void notifyWaitSample() {
		Object lock = new Object();
		
		ArrayList<Thread> threads = startThreads(1, () ->{
			synchronized(lock) {
				try {
					System.out.println("t1 is going to block");
					lock.wait();
					System.out.println("t1 has been notified");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, false);
		
		ArrayList<Thread> threads2 = startThreads(1, () ->{
			//deixa a t1 executar primeiro
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			synchronized(lock) {
				lock.notifyAll();
				System.out.println("t2 has notified");
			}
		}, false);
		
		threads.addAll(threads2);

		joinThreads(threads);
	}
	
	public static void synchronizedSample() {
		//igual ao  mutexSample2 mas utilizando o synchronized

		Object lock = new Object();
		
		final Counter counter = new Counter();
		int iterations = 10 * 1000 * 1000; //10 milh�es
		int batch = 1000;
		Runnable incrementCounter = () -> {
			for(int i = 0; i < iterations / batch; ++i) {
				synchronized(lock) {
					for(int j = 0; j < batch; ++j) {
							counter.Count++;
					}
				}
			}
			
		};

		startThreads(2, incrementCounter);
		
		//!!!!!!!!! Todas as threads t�m que garantir acesso exclusivo � mem�ria, incluindo a thread da aplica��o.
		synchronized(lock) {
			if(counter.Count != iterations * 2) {
				System.out.println("synchronizedSample. Councurrency Problem should never happen. Counter was " + counter.Count + " but should be " + iterations * 2);
			}else {
				System.out.println("synchronizedSample. Councurrency Problem Solved!");
			}
		}
	}
	
	public static void mutexSample2() {
		//no mutexSample os locks est�o a ser adquiridos com uma frequ�ncia muito elevada e isto traz problemas de efici�ncia
		//a este problema chama-se problema de conten��o. � uma situa��o em que o recurso � libertado e adquirido com um ritmo muito acelarado
		//para evitar este problema podemos arranjar uma estrat�gia onde � dada uma oportunidade para fazer v�rias opera��es quando o lock � adquirido
		
		Lock lock = new ReentrantLock(false);
		
		final Counter counter = new Counter();
		int iterations = 10 * 1000 * 1000; //10 milh�es
		int batch = 1000;
		Runnable incrementCounter = () -> {
			for(int i = 0; i < iterations / batch; ++i) {
				lock.lock();
				//quando o lock adquirido uma thread them oportunidade de incrementar o counter batch vezes
				//na situa��o em que o batch � igual ao n�mero de itera��es ent�o as threads t�m que ficar sempre bloquadas at� a thread atual completar toda a opera��o
				try {
					for(int j = 0; j < batch; ++j) {
							counter.Count++;
					}
				}finally {
					lock.unlock();
				}
			}
			
		};

		startThreads(2, incrementCounter);
		
		//!!!!!!!!! Todas as threads t�m que garantir acesso exclusivo � mem�ria, incluindo a thread da aplica��o.
		lock.lock();
		try {
			if(counter.Count != iterations * 2) {
				System.out.println("mutexSample. Councurrency Problem should never happen. Counter was " + counter.Count + " but should be " + iterations * 2);
			}else {
				System.out.println("mutexSample. Councurrency Problem Solved!");
			}
		}finally {
			lock.unlock();
		}
	}
	
	public static void mutexSample() {
		//https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantLock.html
		//"Mutex" em Java. Garante a exclus�o m�tua de execu��o de c�digo entre threads diferentes
		Lock lock = new ReentrantLock(false);
		
		final Counter counter = new Counter();
		int iterations = 10 * 1000 * 1000; //10 milh�es

		Runnable incrementCounter = () -> {
			for(int i = 0; i < iterations; ++i) {
				lock.lock();
				try {
					counter.Count++;
				}finally {
					lock.unlock();
				}
			}
			
		};

		startThreads(2, incrementCounter);
		
		//!!!!!!!!! Todas as threads t�m que garantir acesso exclusivo � mem�ria, incluindo a thread da aplica��o.
		lock.lock();
		try {
			if(counter.Count != iterations * 2) {
				System.out.println("mutexSample. Councurrency Problem should never happen. Counter was " + counter.Count + " but should be " + iterations * 2);
			}else {
				System.out.println("mutexSample. Councurrency Problem Solved!");
			}
		}finally {
			lock.unlock();
		}
	}
	
	public static void concurrencyProblem() {
		//Se duas threads t�m acesso � mesma zona de mem�ria, ent�o o acesso tem que ser sincronizado
		
		final Counter counter = new Counter();
		int iterations = 10 * 1000 * 1000; //10 milh�es
		
		Runnable incrementCounter = () -> {
			for(int i = 0; i < iterations; ++i) {
				counter.Count++;
			}
		};
		
		startThreads(2, incrementCounter);
		
		if(counter.Count != iterations * 2) {
			System.out.println("Councurrency Problem. Counter was " + counter.Count + " but should be " + iterations * 2);
		}else {
			System.out.println("Councurrency Problem. There wasn't a problem after all");
		}
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < 5; ++i) {
			//Execu��es diferentes t�m resultados diferentes
			concurrencyProblem();
		}
		System.out.println("");
		
		//Execu��es diferentes t�m resultados iguais
		long time = System.currentTimeMillis();
		mutexSample();
		long elapsed = System.currentTimeMillis() - time;
		System.out.println("mutexSample time = " + elapsed);
		System.out.println("");

		time = System.currentTimeMillis();
		mutexSample2();
		elapsed = System.currentTimeMillis() - time;
		System.out.println("mutexSample2 time = " + elapsed);
		System.out.println("");

		time = System.currentTimeMillis();
		synchronizedSample();
		elapsed = System.currentTimeMillis() - time;
		System.out.println("synchronized time = " + elapsed);
		System.out.println("");

		for(int i = 0; i < 5; ++i) {
			notifyWaitSample();
		}
		
		deadLock();
		System.out.println("Finnished");
	}
	
}
