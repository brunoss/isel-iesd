package org.mdeos.osgi.helloclient;

import org.mdeos.osgi.helloapi.HelloWorld;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

public class HelloWorldClient implements BundleActivator, ServiceListener {
	private HelloWorld helloWorld = null;
	private BundleContext context = null;
	private ServiceReference<?> serviceReference = null;
	private final String MSG = "Uma cultura de produto (nacional) pode salvar-nos...";

	public void start(BundleContext context) throws Exception {
		this.context = context;
		String filter;

		System.out.println("start(): Starting " + context.getBundle().getSymbolicName() + " bundle...");
		filter = "(|(objectClass=" + HelloWorld.class.getName() + "))";
		context.addServiceListener(this, filter);
		// Testa se o serviço HelloWorld já estava registado
		serviceReference = context.getServiceReference(HelloWorld.class.getName());
		helloWorld = (HelloWorld) context.getService(serviceReference);
		outputHelloMessade(helloWorld);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping " + context.getBundle().getSymbolicName() + "...");
		if (serviceReference != null) {
			context.ungetService(serviceReference);
		}
		context.removeServiceListener(this);
	}

	public void serviceChanged(ServiceEvent serviceEvent) {
		ServiceReference<?> serviceReference = serviceEvent.getServiceReference();
		Object serviceObject = context.getService(serviceReference);

		System.out.println("HelloWorldClient.serviceChanged(): ServiceEvent received...");
		switch (serviceEvent.getType()) {
		case (ServiceEvent.MODIFIED): {
			System.out.println("MODIFIED: ");
			break;
		}
		case (ServiceEvent.MODIFIED_ENDMATCH): {
			System.out.println("MODIFIED_ENDMATCH: ");
			break;
		}
		case (ServiceEvent.REGISTERED): {
			System.out.print("REGISTERED: ");
			if (serviceObject instanceof HelloWorld) {
				System.out.println("HelloWorld service registered...");
				helloWorld = (HelloWorld) serviceObject;
				outputHelloMessade(helloWorld);
			} else
				System.out.println("Registered some other service...");
			break;
		}
		case (ServiceEvent.UNREGISTERING): {
			System.out.print("UNREGISTERING: ");
			if (serviceObject instanceof HelloWorld) {
				System.out.println("HelloWorld service");
			} else
				System.out.println("Other service");
			break;
		}
		default: {
			System.out.println("Event not identified...");
		}
		}
	}
	void outputHelloMessade(HelloWorld helloworld) {
		if (helloWorld != null) {
			System.out.println("Calling the HelloWorld service...");
			System.out.println("Received: " + helloWorld.sayHello(MSG));
		} else
			System.out.println("WARNING: helloWorldService = NULL...");
	}
}
