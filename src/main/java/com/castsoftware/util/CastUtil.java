package com.castsoftware.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

public class CastUtil {
    
	public static String formatNanoTime(long nanoTime)
	{
		long seconds = TimeUnit.SECONDS.convert(nanoTime, TimeUnit.NANOSECONDS);
		long minutes = seconds / 60;
		seconds = seconds % 60;
		long hours = minutes / 60;
		minutes = minutes % 60;
		String returnValue = "";
		
		if (hours > 0)
			returnValue = hours + "h";
		if ((hours > 0) || (minutes > 0))
			returnValue += minutes + "m";
		if ((hours > 0) || (minutes > 0) || (seconds > 0))
			returnValue += seconds + "s";
		
		return returnValue;
	}
	
	public static String jsonIndentation(int level)
	{
		String s = "";
		for(int i=0;i<level;i++)
			s += "  ";
		return s;
	}
	
	public static int extractIdFromHref(String href)
	{
		return Integer.parseInt(href.substring(href.lastIndexOf("/") + 1));		
	}
	
	public static String extractIdFromHrefAsString(String href)
	{
		return href.substring(href.lastIndexOf("/") + 1);		
	}
	
	public static CompareResults compareJsonStrings(String expected, String actual)
	{
		try
		{
			JSONCompareResult result = JSONCompare.compareJSON(expected, actual, JSONCompareMode.LENIENT);
			return new CompareResults(!result.failed(), result.getMessage());
		} catch (JSONException e) {
			return new CompareResults(false, e.getMessage());
		}
	}
	
	public static List<String> customSplit(String input, char delimiter) 
	{
		   List<String> elements = new ArrayList<String>();       
		   StringBuilder elementBuilder = new StringBuilder();

		   boolean isQuoted = false;
		   for (char c : input.toCharArray()) {
		     if (c == '\"') {
		        isQuoted = !isQuoted;
		        // continue;        // changed according to the OP comment - \" shall not be skipped
		     }
		     if (c == delimiter && !isQuoted) {
		        elements.add(elementBuilder.toString().trim());
		        elementBuilder = new StringBuilder();
		        continue;
		     }
		     elementBuilder.append(c); 
		   }
		   elements.add(elementBuilder.toString().trim()); 
		   return elements;
		}

}
