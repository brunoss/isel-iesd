package org.mdeos.osgi.helloworld.impl;

import org.mdeos.osgi.helloapi.HelloWorld;

/**
 * @author MDEOS example
 */
public class HelloWorldImpl implements HelloWorld {
	public String sayHello(String msg) {
		System.out.println("HelloWorldImpl.sayHelloWorld(): " + msg);
		return msg.toUpperCase();
	}
}
