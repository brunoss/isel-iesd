package org.mdeos.osgi.dservices;

import org.mdeos.osgi.transaction.ITransactionManager;
import org.mdeos.osgi.transaction.OperationIdentifier;
import org.mdeos.osgi.vectorapi.IVector;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

/**
 * @author org.mdeos
 */
@Component(name = "org.mdeos.osgi.helloapi.HelloWorld", immediate = true, service = IVector.class, property = {
		// Remote Services configuration
		"service.exported.interfaces=*", "service.exported.configs=org.apache.cxf.ws",
		"org.apache.cxf.ws.address=http://generoso2:9011/Vector",

		// Service meta-data definition
		"ServiceName=HelloWorld", "ServiceVersion=1.0.0", "ServiceProvider=osgi.helloworldservices", })
public class VectorImpl implements IVector {
    private static List<Integer> vector = Arrays.asList(300, 234, 56, 789);
	
	@Reference(bind = "bindTranslator")
	ITransactionManager manager;
	ComponentContext componentContext = null;

	public VectorImpl() {
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
	public int read(String token, int idx) {
		Collection<OperationIdentifier> operation = manager.identifyTransaction(token);
		return vector.get(idx);
	}

	@Override
	public void write(String token, int idx, int value) {
		Collection<OperationIdentifier> operation = manager.identifyTransaction(token);
		vector.set(idx, value);
	}
}
