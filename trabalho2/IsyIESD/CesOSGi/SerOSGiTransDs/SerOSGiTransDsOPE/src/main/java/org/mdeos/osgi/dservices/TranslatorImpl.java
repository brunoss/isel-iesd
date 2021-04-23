package org.mdeos.osgi.dservices;

import java.util.Map;

import org.mdeos.osgi.translatorapi.Translator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author org.mdeos
 */
@Component(name = "org.mdeos.osgi.translatorapi.Translator", immediate = true, service = Translator.class, property = {
		"ServiceName=Translator", "ServiceVersion=1.0.0", "ServiceProvider=org.mdeos.osgi.simple-examples", })
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
		// Poderá invocar o serviço de tradução (Translate) se obtiver uma chave de acesso
		// e.g., Microsoft Azure via conta de e-mail ISEL
		return "Translation remote service temporarily unavailable...".toUpperCase();
	}
}
