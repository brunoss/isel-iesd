package org.mdeos.osgi.helloclient;

import org.mdeos.osgi.helloapi.HelloWorld;
import org.mdeos.osgi.helloimpl.HelloWorldImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class HelloWorldClient implements BundleActivator{
	
	public void start(BundleContext context) throws Exception {
		HelloWorld helloWorld =  new HelloWorldImpl();
		System.out.println("HelloWorldClient.start(): " + helloWorld.sayHello("## Hello World! ##"));
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping HelloWorldClient bundle...");
	}
}
