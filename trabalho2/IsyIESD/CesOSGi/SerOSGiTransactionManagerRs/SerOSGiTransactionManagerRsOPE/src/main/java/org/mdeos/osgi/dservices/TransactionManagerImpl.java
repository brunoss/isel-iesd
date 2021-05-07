package org.mdeos.osgi.dservices;

import org.mdeos.osgi.transaction.*;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import java.util.stream.Collectors;

/**
 * @author org.mdeos
 */
@Component(name = "org.mdeos.osgi.helloapi.HelloWorld", immediate = true, service = ITransactionManager.class, property = {
		// Remote Services configuration
		"service.exported.interfaces=*", "service.exported.configs=org.apache.cxf.ws",
		"org.apache.cxf.ws.address=http://generoso2:9011/Vector",

		// Service meta-data definition
		"ServiceName=HelloWorld", "ServiceVersion=1.0.0", "ServiceProvider=osgi.helloworldservices", })
public class TransactionManagerImpl implements ITransactionManager {
	private static final Map<String, Collection<OperationIdentifier>> map = new HashMap<String, Collection<OperationIdentifier>>();

	@Reference(bind = "bindTranslator")
	ITransactionManager manager;
	ComponentContext componentContext = null;

	public TransactionManagerImpl() {
		System.out.println("## HelloWorldImpl contructor of ...");
	}

	@Activate
	void activate(BundleContext bundleContext, ComponentContext componentContext) {
		this.componentContext = componentContext;
		System.out.println("SERVICE :: HelloWorldImpl.activate()..." + this.componentContext.toString());
		Dictionary<String, Object> properties = componentContext.getProperties();
		System.out.println("SERVICE :: HelloWorld service properties:");
		for (Enumeration<String> e = properties.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			System.out.println("<" + key.toString() + ", " + properties.get(key) + ">");
		}
	}

	@Deactivate
	void deactivate(ComponentContext componentContext) {
		System.out.println("SERVICE :: HelloWorldImpl.deactivate()...");
		this.componentContext = null;
	}

	public void bindTranslator(ITransactionManager manager) {
		System.out.println("SERVICE :: HelloWorldImpl.bindTranslator() called...");
		this.manager = manager;
	}

	@Override
	public String getToken(Collection<OperationIdentifier> operations) {
		String guid = java.util.UUID.randomUUID().toString();
		map.put(guid, operations);
		return guid;
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
	  
	@Override
	public Collection<OperationIdentifier> identifyTransaction(String token) {
		Collection<OperationIdentifier> operations = map.get(token);
		
		Stream<OperationIdentifier> stream = map.values().stream().flatMap(Collection::stream);
		Collection<OperationIdentifier> allOperations = stream.collect(Collectors.toList());
		for(OperationIdentifier identifier : operations) {
			if(allOperations.contains(identifier)) {
				while(!createFile()) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return operations;
	}

	@Override
	public void commit(String token) {
		deleteFile();
		map.remove(token);
	}
}
