package com.castsoftware.restapi;

public class JsonResponse {
	private final int code;
	private final String jsonString;
	
	public int getCode() { return code;}
	public String getJsonString() { return jsonString;}
	
	public JsonResponse(int code, String jsonString)
	{
		this.code = code;
		this.jsonString = jsonString;			
	}
}
