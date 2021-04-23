package org.mdeos.osgi.helloworld.client;

import org.mdeos.osgi.helloapi.HelloWorld;
import org.mdeos.osgi.helloworld.impl.HelloWorldImpl;

public class HelloWorldClient {
	
	 public static void main(String[] args) {
		HelloWorld helloWorld =  new HelloWorldImpl();
		System.out.println("HelloWorldClient.start(): " + helloWorld.sayHello("## Hello World! ##"));
	}
}
