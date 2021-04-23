package org.mdeos.osgi.dservices;

import java.util.Map;

import org.mdeos.osgi.translatorapi.Translator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
// The component name is used to generate the Declarative Services XML descriptor file
// stored into the bundle file:
// service.ddstranslatorimpl-1.0.0.jar\OSGI-INF\org.mdeos.osgi.translatorapi.Translator.xml
@Component(name = "org.mdeos.osgi.translatorapi.Translator", immediate = true,
service = Translator.class,
property = {
		// Remote Services configuration
		  "service.exported.interfaces=*",
		  "service.exported.configs=org.apache.cxf.ws",
		  "org.apache.cxf.ws.address=http://generoso2:9020/Translator",
		  
		// Service meta-data definition
		  "ServiceName=Translator",
		  "ServiceVersion=1.0.0",
		  "ServiceProvider=osgi.helloworldservices",
})
public class TranslatorImpl implements Translator {

	public TranslatorImpl() {
		System.out.println("## TranslatorImpl contructor...");
	}

	@Activate
	void activate(Map<?, ?> properties) {
		System.out.println("SERVICE :: TranslatorImpl.activate()...");
		try {
			System.out.println("Returned from TranslatorImpl.translate():" + this.translate("the message..."));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Deactivate
	void deactivate(Map<?, ?> properties) {
		System.out.println("SERVICE :: TranslatorImpl.deactivate()...");
	}

	public String translate(String msg) throws Exception {
		System.out.println("SERVICE :: TranslatorImpl.translate()...");
		// PoderÃ¡ invocar o serviço de tradução (Translate) se obtiver uma chave de acesso
		// e.g., Microsoft Azure via conta de e-mail ISEL
		return "Translation remote service temporarily unavailable...".toUpperCase();
	}
}
