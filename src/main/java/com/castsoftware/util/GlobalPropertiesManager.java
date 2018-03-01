package com.castsoftware.util;

public class GlobalPropertiesManager {

	private static GlobalProperties globalProperties;
	
	static
	{
		globalProperties = new GlobalProperties();		
	}
	
	public static GlobalProperties getGlobalProperties() {
	    return globalProperties;
	  }

}
