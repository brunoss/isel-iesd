package org.mdeos.osgi.helloimpl;

import org.mdeos.osgi.helloapi.HelloWorld;

/**
 * @author MDEOS example
 */
public class HelloWorldImpl implements HelloWorld {
	public String sayHello(String msg) {
		System.out.println("SERVICE :: HelloWorldImpl.sayHelloWorld(): " + msg);
		return msg.toUpperCase();
	}
}
