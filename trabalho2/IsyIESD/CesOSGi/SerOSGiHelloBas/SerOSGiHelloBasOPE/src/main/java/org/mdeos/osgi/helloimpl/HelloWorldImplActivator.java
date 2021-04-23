package org.mdeos.osgi.helloimpl;

import java.util.Dictionary;
import java.util.Hashtable;

import org.mdeos.osgi.helloapi.HelloWorld;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class HelloWorldImplActivator implements BundleActivator  {
	
	public void start(BundleContext context) throws Exception {
    	Dictionary<String, String>  metadata = new Hashtable<String, String>();
    	
		System.out.println("Starting " + context.getBundle().getSymbolicName() + " bundle...");
    	HelloWorld helloWorld = new HelloWorldImpl();
    	
    	metadata.put("ServiceName", "HelloWorld");
    	metadata.put("ServiceVersion", "1.0.0");
    	metadata.put("ServiceProvider", "IESD");
    	
    	context.registerService(HelloWorld.class.getName(), helloWorld, metadata);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping " + context.getBundle().getSymbolicName() + " bundle...");
		context.ungetService(context.getServiceReference(HelloWorld.class.getName()));
	}
}
