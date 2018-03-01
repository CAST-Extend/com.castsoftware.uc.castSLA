package com.castsoftware.util;

public class ParametersHelper {
	public static String getParameter(String[] args, String name)
	{
		int len = args.length;
		for(int i=1; i<len; i++)
		{
			if (getParameterName(args[i]).equals(name))
			{
				return getParameterValue(args[i]);
			}
		}
		return "";
	}
	
	private static String getParameterName(String parameter)
	{
		return parameter.substring(0, parameter.indexOf("=")); 
	}
	
	private static String getParameterValue(String parameter)
	{
		return parameter.substring(parameter.indexOf("=") + 1); 
	}
}
