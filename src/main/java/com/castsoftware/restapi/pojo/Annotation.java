package com.castsoftware.restapi.pojo;

public class Annotation {
	public String version;
	public JsonDate date;
	public String descriptions;
	public String name;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public JsonDate getDate() {
		return date;
	}
	public void setDate(JsonDate date) {
		this.date = date;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
