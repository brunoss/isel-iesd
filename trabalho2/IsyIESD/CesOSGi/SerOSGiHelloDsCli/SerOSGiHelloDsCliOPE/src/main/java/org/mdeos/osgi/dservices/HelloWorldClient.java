package org.mdeos.osgi.dservices;

import org.mdeos.osgi.helloapi.HelloWorld;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

@Component(name = "org.mdeos.osgi.dservices.HelloWorldClient", immediate = true)
public class HelloWorldClient {

    @Reference(bind = "bindHelloworld")
    HelloWorld helloWorld;
    ComponentContext componentContext;

    public HelloWorldClient() {
        System.out.println("## HelloWorldClient contructor...");
    }

    @Activate
    void activate(ComponentContext componentContext) { // role of start()
        System.out.println("CES :: HelloWorldClient.activate()...");
        this.componentContext = componentContext;
        // calling sayHello()
        System.out.println("Return from HelloWorldClient.activate(): " + helloWorld.sayHello("Hello World!"));
    }

    @Deactivate
    void deactivate(ComponentContext componentContext) {
        System.out.println("CES :: HelloWorldClient.deactivate() called...");
        this.componentContext = null;
    }

    public void bindHelloworld(HelloWorld helloWorld) {
        System.out.println("CES :: HelloWorldClient.bindHelloworld() called...");
        this.helloWorld = helloWorld;
    }
}
