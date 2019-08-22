package com.castsoftware.restapi.pojo;

import java.util.List;

public class Application {
    private String href;
    private String name;
    private List<String> technologies;
    private SimpleReference systems;
    private SimpleReference modules;
    private SimpleReference snapshots;
    private SimpleReference results;
    private String origin;
    private String adgDatabase;
    private String adgWebSite;
    private String adgLocalId;
    private String adgVersion;
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
	public List<String> getTechnologies() {
		return technologies;
	}
	public void setTechnologies(List<String> technologies) {
		this.technologies = technologies;
	}
	public SimpleReference getSystems() {
		return systems;
	}
	public void setSystems(SimpleReference systems) {
		this.systems = systems;
	}
	public SimpleReference getModules() {
		return modules;
	}
	public void setModules(SimpleReference modules) {
		this.modules = modules;
	}
	public SimpleReference getSnapshots() {
		return snapshots;
	}
	public void setSnapshots(SimpleReference snapshots) {
		this.snapshots = snapshots;
	}
	public SimpleReference getResults() {
		return results;
	}
	public void setResults(SimpleReference results) {
		this.results = results;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getAdgDatabase() {
		return adgDatabase;
	}
	public void setAdgDatabase(String adgDatabase) {
		this.adgDatabase = adgDatabase;
	}
	public String getAdgWebSite() {
		return adgWebSite;
	}
	public void setAdgWebSite(String adgWebSite) {
		this.adgWebSite = adgWebSite;
	}
	public String getAdgLocalId() {
		return adgLocalId;
	}
	public void setAdgLocalId(String adgLocalId) {
		this.adgLocalId = adgLocalId;
	}
	public String getAdgVersion() {
		return adgVersion;
	}
	public void setAdgVersion(String adgVersion) {
		this.adgVersion = adgVersion;
	}
    
    
}
