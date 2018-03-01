package com.castsoftware.util;

public class CompareResults {
	private boolean success;
	private String message;
	
	public CompareResults(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
	
	public boolean isSuccessful(){
		return success;
	}
	
	public String getMessage() {
		return message;
	}
}
