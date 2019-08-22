package com.castsoftware.restapi.pojo;

import java.util.List;

public class Metric {
	private int number;
	private JsonDate date;
	private Application application;
	private SimpleReference applicationSnapshot;
	private List<ApplicationResult> applicationResults;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public JsonDate getDate() {
		return date;
	}
	public void setDate(JsonDate date) {
		this.date = date;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public SimpleReference getApplicationSnapshot() {
		return applicationSnapshot;
	}
	public void setApplicationSnapshot(SimpleReference applicationSnapshot) {
		this.applicationSnapshot = applicationSnapshot;
	}
	public List<ApplicationResult> getApplicationResults() {
		return applicationResults;
	}
	public void setApplicationResults(List<ApplicationResult> applicationResults) {
		this.applicationResults = applicationResults;
	}
	
	
}
