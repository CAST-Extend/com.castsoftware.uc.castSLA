package com.castsoftware.restapi.pojo;

import java.util.List;

public class Module {
    private String href;
    private String name;
    private List<String> technologies;
    private SimpleReference applications;
    private SimpleReference snapshots;
    private SimpleReference results;
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
	public SimpleReference getApplications() {
		return applications;
	}
	public void setApplications(SimpleReference applications) {
		this.applications = applications;
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

    
    
}
