package com.zeon.server.core;

import java.io.FileInputStream;
import java.util.Properties;

public class JavaServerProperties {

	private Properties properties;

	public void loadProperties() throws Exception {
		properties = readPropertiesFromFile();
	}

	public Properties readPropertiesFromFile() throws Exception {
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream("./config/config.xml");
		properties.loadFromXML(fis);
		return properties;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public boolean getBoolen(String key) {
		String property = getProperty(key);
		return property.equals("true");
	}

	public int getInt(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public long getLong(String key) {
		return Long.parseLong(getProperty(key));
	}

	public float getFloat(String key) {
		return Float.parseFloat(getProperty(key));
	}
}
