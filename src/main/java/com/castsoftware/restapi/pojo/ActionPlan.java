package com.castsoftware.restapi.pojo;

public class ActionPlan {
	private SimpleReference issues;//TODO: check type
	private SimpleReference summary;
	
	public SimpleReference getIssues() {
		return issues;
	}
	public void setIssues(SimpleReference issues) {
		this.issues = issues;
	}
	public SimpleReference getSummary() {
		return summary;
	}
	public void setSummary(SimpleReference summary) {
		this.summary = summary;
	}
	
	
}
