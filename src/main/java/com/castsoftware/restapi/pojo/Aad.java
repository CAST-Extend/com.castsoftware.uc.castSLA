package com.castsoftware.restapi.pojo;

public class Aad {
	public String href;
	public String name;
	public String dbType;
	public String version;
	public String dbmsVersion;
	public SimpleReference systems;
	public SimpleReference applications;
	public SimpleReference configurations;
	public SimpleReference results;
	public SimpleReference commonCategories;
	public SimpleReference tags;
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDbmsVersion() {
		return dbmsVersion;
	}
	public void setDbmsVersion(String dbmsVersion) {
		this.dbmsVersion = dbmsVersion;
	}
	public SimpleReference getSystems() {
		return systems;
	}
	public void setSystems(SimpleReference systems) {
		this.systems = systems;
	}
	public SimpleReference getApplications() {
		return applications;
	}
	public void setApplications(SimpleReference applications) {
		this.applications = applications;
	}
	public SimpleReference getConfigurations() {
		return configurations;
	}
	public void setConfigurations(SimpleReference configurations) {
		this.configurations = configurations;
	}
	public SimpleReference getResults() {
		return results;
	}
	public void setResults(SimpleReference results) {
		this.results = results;
	}
	public SimpleReference getCommonCategories() {
		return commonCategories;
	}
	public void setCommonCategories(SimpleReference commonCategories) {
		this.commonCategories = commonCategories;
	}
	public SimpleReference getTags() {
		return tags;
	}
	public void setTags(SimpleReference tags) {
		this.tags = tags;
	}
	
	
}
