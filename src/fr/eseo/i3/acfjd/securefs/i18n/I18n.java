package fr.eseo.i3.acfjd.securefs.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18n {

	private static final String BUNDLE_NAME = I18n.class.getPackageName()+".messages";
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(I18n.BUNDLE_NAME);
	
	private I18n() {}
	
	public static String getI18n(String key) {
		try {
			return I18n.RESOURCE_BUNDLE.getString(key);
		}
		catch(final MissingResourceException ex) {
			return "Missing key: "+key;
		}
	}
}
