package com.castsoftware.restapi.pojo;

import java.util.List;

public class ApplicationResult {
    private String type;
    private Reference reference;
    private Result result;
    private List<String> technologyResults;
    private List<String> moduleResults;
    private List<String> transactionResults;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Reference getReference() {
		return reference;
	}
	public void setReference(Reference reference) {
		this.reference = reference;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
	public List<String> getTechnologyResults() {
		return technologyResults;
	}
	public void setTechnologyResults(List<String> technologyResults) {
		this.technologyResults = technologyResults;
	}
	public List<String> getModuleResults() {
		return moduleResults;
	}
	public void setModuleResults(List<String> moduleResults) {
		this.moduleResults = moduleResults;
	}
	public List<String> getTransactionResults() {
		return transactionResults;
	}
	public void setTransactionResults(List<String> transactionResults) {
		this.transactionResults = transactionResults;
	}
    
    
}
