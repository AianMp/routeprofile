package es.udc.muei.riws.routeprofile.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigurationParametersManager {

    private static final String CONFIGURATION_FILE = "lucene.properties";
    private static Map<String, String> parameters;

    private ConfigurationParametersManager() {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static synchronized Map<String, String> getParameters() {
	if (parameters == null) {
	    Class<ConfigurationParametersManager> configurationParametersManagerClass = ConfigurationParametersManager.class;
	    ClassLoader classLoader = configurationParametersManagerClass.getClassLoader();
	    InputStream inputStream = classLoader.getResourceAsStream(CONFIGURATION_FILE);
	    Properties properties = new Properties();
	    try {
		properties.load(inputStream);
		inputStream.close();
	    } catch (IOException e) {
		throw new RuntimeException(e);
	    }
	    parameters = new HashMap(properties);
	}
	return parameters;
    }

    public static String getParameter(String name) {
	return getParameters().get(name);
    }
}