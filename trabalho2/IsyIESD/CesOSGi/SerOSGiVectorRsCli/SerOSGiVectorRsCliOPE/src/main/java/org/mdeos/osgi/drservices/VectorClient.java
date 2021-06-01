package org.mdeos.osgi.drservices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.mdeos.osgi.transaction.ILockManager;
import org.mdeos.osgi.transaction.ITransactionManager;
import org.mdeos.osgi.transaction.Operation;
import org.mdeos.osgi.transaction.OperationIdentifier;
import org.mdeos.osgi.vectorapi.*;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(name = "org.mdeos.osgi.drservices.HelloWorldClient", immediate = true)
public class VectorClient {
	
	static final Random r = new Random();

    @Reference(bind = "bindVectors")
    List<IVector> vectors;
    @Reference(bind = "bindTransactionManager")
    ITransactionManager transactionManager;
    @Reference(bind = "bindLockManager")
    ILockManager lockManager;
    
    ComponentContext componentContext;

    
    public VectorClient() {
        System.out.println("## HelloWorldClient contructor...");
    }

    private int getRandom(int n) {
    	for(;;) {
    		int random = r.nextInt(vectors.size());
    		if(random != n) {
    			return random;
    		}
    	}
    }
    
    private void ReadAndWriteValues() 
    {
        int random = r.nextInt(vectors.size());
        IVector vector1 = vectors.get(random);
        random = getRandom(random);
        IVector vector2 = vectors.get(random);
        
        Collection<OperationIdentifier> operations = new ArrayList<OperationIdentifier>();
        
        OperationIdentifier read1 = new OperationIdentifier();
        read1.Server = vector1.toString();
        read1.Operation = Operation.Read;
        operations.add(read1);
        
        OperationIdentifier write1 = new OperationIdentifier();
        write1.Server = vector1.toString();
        write1.Operation = Operation.Write;
        operations.add(write1);
        
        OperationIdentifier read2 = new OperationIdentifier();
        read2.Server = vector2.toString();
        read2.Operation = Operation.Read;
        operations.add(read2);
        
        OperationIdentifier write2 = new OperationIdentifier();
        write2.Server = vector2.toString();
        write2.Operation = Operation.Write;
        operations.add(write2);
        
        while(true) {
            String token = transactionManager.getToken(operations);
            
            random = r.nextInt(4);
            int value = vector1.read(token, random);
            int ammount = r.nextInt(value / 2);
            
            random = getRandom(random);
            vector1.write(token, random, value - ammount);

            random = r.nextInt(4);
            value = vector2.read(token, random);
            random = getRandom(random);
            vector2.write(token, random, value + ammount);
            
            transactionManager.commit(token);
        }
    }
    
    void testLockManager() {
        Collection<OperationIdentifier> operations = new ArrayList<OperationIdentifier>();
        OperationIdentifier writeOperation = new OperationIdentifier();
        writeOperation.Position = 0;
        writeOperation.Server = "localhost";
        writeOperation.Operation = Operation.Write;
        operations.add(writeOperation);

    	lockManager.getLocks("1", operations, null);
    }
    
    @Activate
    void activate(ComponentContext componentContext) { // role of start()
        System.out.println("CES :: HelloWorldClient.activate()...");
        this.componentContext = componentContext;
        
        testLockManager();
    }
    
    @Deactivate
    void deactivate(ComponentContext componentContext) {
        System.out.println("CES :: HelloWorldClient.deactivate() called...");
        this.componentContext = null;
    }

    public void bindVectors(List<IVector> vectors) {
        System.out.println("CES :: HelloWorldClient.bindHelloworld() called...");
        this.vectors = vectors;
    }
    
    public void bindTransactionManager(ITransactionManager transactionManager) {
    	this.transactionManager = transactionManager;
    }
    
    public void bindLockManager(ILockManager lockManager) {
    	this.lockManager = lockManager;
    }
}
