package com.castsoftware.jenkins.CastSLA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

public class HealthFactorConditionParam  extends AbstractDescribableImpl<HealthFactorConditionParam>  {
	private final String metric;
    private final String operator;
    private final String value;
    
    private static final List<String> metrics = new ArrayList<String>(Arrays.asList(
    		"TQI","Transferability","Changeability",
    		"Robustness","Performance","Security",
    		"Critical Violations", "Critical Violations on new and modified code"));
    
    private static final List<Integer> metricIds = new ArrayList<Integer>(Arrays.asList(
    		60017, 60011, 60012, 
    		60013, 60014, 60016, 
    		67011, 67015));
    
    private static final List<String> metricGroups = new ArrayList<String>(Arrays.asList(
    		"quality-indicators", "quality-indicators", "quality-indicators", 
    		"quality-indicators", "quality-indicators", "quality-indicators", 
    		"sizing-measures", "sizing-measures"));
    
    private static final List<String> metricFields = new ArrayList<String>(Arrays.asList(
    		"grade", "grade", "grade", 
    		"grade", "grade", "grade", 
    		"value", "value"));
   
    private static final List<String> operators = new ArrayList<String>(Arrays.asList(
    		">",">=","=","!=","<=","<","Change"));

    @DataBoundConstructor
    public HealthFactorConditionParam(String metric, String operator, String value) {
	    this.metric = metric;
	    this.operator = operator;
	    this.value = value;
    }
   
    public String getMetric() {
        return metric;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public String getValue() {
    	return value;
    }
    
    public Double getValueAsDouble() {
    	return Double.parseDouble(value);
    }
    
    public int getMetricId(){
    	return metricIds.get(Integer.parseInt(metric));
    }
    
    public String getMetricGroup(){
    	return metricGroups.get(Integer.parseInt(metric));
    }

    public String getMetricField(){
    	return metricFields.get(Integer.parseInt(metric));
    }
        
    public String getMetricName(){
    	return metrics.get(Integer.parseInt(metric));
    }
    
    public String getOperatorName(){
    	return operators.get(Integer.parseInt(operator));
    }
    
    @Override
    public String toString(){
    	return String.format("%s %s %s",getMetricName(), getOperatorName(), value);
    }
       
    @Extension
    public static class DescriptorImpl extends Descriptor<HealthFactorConditionParam> {
        public String getDisplayName() { return "Metric Condition"; }
        
        public ListBoxModel doFillMetricItems()
        {
        	ListBoxModel m = new ListBoxModel();
        	int count = metrics.size();
        	for(int i=0;i<count;i++)
        	{
        		m.add(metrics.get(i), Integer.toString(i));        		
        	}
        	return m;
        }
        
        public ListBoxModel doFillOperatorItems()
        {
        	ListBoxModel m = new ListBoxModel();
        	int count = operators.size();
        	for(int i=0;i<count;i++)
        	{
        		m.add(operators.get(i), Integer.toString(i));        		
        	}
        	return m;
        }
        
        public FormValidation doCheckValue(@QueryParameter String value)
                throws IOException, ServletException {
        	try {
        		Double.parseDouble(value);
				return FormValidation.ok();
			} catch (NumberFormatException e) {
				return FormValidation.error("Number expected!");
			}
        }
    } 
}
