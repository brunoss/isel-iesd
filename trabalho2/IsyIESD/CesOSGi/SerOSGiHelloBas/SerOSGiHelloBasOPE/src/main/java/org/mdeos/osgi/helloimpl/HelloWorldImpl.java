package org.mdeos.osgi.helloimpl;

import org.mdeos.osgi.helloapi.HelloWorld;

/**
 * @author iesd
 */
public class HelloWorldImpl implements HelloWorld {

	public HelloWorldImpl() {
		System.out.println("## Constructor of HelloWorldImpl...context.toString() = ");
	}

	public String sayHello(String msg) {
		System.out.println("SERVICE :: In HelloWorldImpl.sayHelloWorld(): " + msg);
		return msg.toUpperCase();
	}
}
