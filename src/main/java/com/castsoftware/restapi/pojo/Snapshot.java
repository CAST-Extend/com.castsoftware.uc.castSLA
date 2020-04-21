package com.castsoftware.restapi.pojo;

import java.util.List;

public class Snapshot implements Comparable<Snapshot>{
	private String href;
	private String name;
	private String number;
	
	private List<String> technologies;
	private Annotation annotation;
	private SimpleReference configurationSnapshot;
	private SimpleReference systems;
	private Application application;
	private SimpleReference moduleSnapshots;
	private SimpleReference results;
	private ActionPlan actionPlan;
	private SimpleReference components;
	private SimpleReference transactions;	

	public int compareTo(Snapshot o) {
		return o.annotation.getDate().getAsDate().compareTo(annotation.getDate().getAsDate());
	}

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<String> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(List<String> technologies) {
		this.technologies = technologies;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public SimpleReference getConfigurationSnapshot() {
		return configurationSnapshot;
	}

	public void setConfigurationSnapshot(SimpleReference configurationSnapshot) {
		this.configurationSnapshot = configurationSnapshot;
	}

	public SimpleReference getSystems() {
		return systems;
	}

	public void setSystems(SimpleReference systems) {
		this.systems = systems;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public SimpleReference getModuleSnapshots() {
		return moduleSnapshots;
	}

	public void setModuleSnapshots(SimpleReference moduleSnapshots) {
		this.moduleSnapshots = moduleSnapshots;
	}

	public SimpleReference getResults() {
		return results;
	}

	public void setResults(SimpleReference results) {
		this.results = results;
	}

	public ActionPlan getActionPlan() {
		return actionPlan;
	}

	public void setActionPlan(ActionPlan actionPlan) {
		this.actionPlan = actionPlan;
	}

	public SimpleReference getComponents() {
		return components;
	}

	public void setComponents(SimpleReference components) {
		this.components = components;
	}

	public SimpleReference getTransactions() {
		return transactions;
	}

	public void setTransactions(SimpleReference transactions) {
		this.transactions = transactions;
	}	
	
	
	
}
