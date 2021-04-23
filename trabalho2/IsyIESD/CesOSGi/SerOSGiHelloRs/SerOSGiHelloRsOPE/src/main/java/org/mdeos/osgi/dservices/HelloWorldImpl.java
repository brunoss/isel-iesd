package org.mdeos.osgi.dservices;

import org.mdeos.osgi.helloapi.HelloWorld;
import org.mdeos.osgi.translatorapi.Translator;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import java.util.Dictionary;
import java.util.Enumeration;

/**
 * @author org.mdeos
 */
@Component(name = "org.mdeos.osgi.helloapi.HelloWorld", immediate = true, service = HelloWorld.class, property = {
		// Remote Services configuration
		"service.exported.interfaces=*", "service.exported.configs=org.apache.cxf.ws",
		"org.apache.cxf.ws.address=http://generoso2:9010/HelloWorld",

		// Service meta-data definition
		"ServiceName=HelloWorld", "ServiceVersion=1.0.0", "ServiceProvider=osgi.helloworldservices", })
public class HelloWorldImpl implements HelloWorld {

	@Reference(bind = "bindTranslator")
	Translator translator;
	ComponentContext componentContext = null;

	public HelloWorldImpl() {
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

	public void bindTranslator(Translator translator) {
		System.out.println("SERVICE :: HelloWorldImpl.bindTranslator() called...");
		this.translator = translator;
	}

	public String sayHello(String msg) {
		System.out.println("SERVICE :: HelloWorldImpl.sayHello(): " + msg);
		try {
			System.out.println(
					"Returned from calling translator.translate()..." + translator.translate("to be translated..."));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg.toUpperCase();
	}
}
