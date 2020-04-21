package com.castsoftware.restapi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.castsoftware.util.CastUtil;
import com.castsoftware.restapi.pojo.Aad;
import com.castsoftware.restapi.pojo.Application;
import com.castsoftware.restapi.pojo.Metric;
import com.castsoftware.restapi.pojo.Module;
import com.castsoftware.restapi.pojo.Snapshot;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import hudson.model.BuildListener;

public class CastRest {
	public static boolean debugMode=false;
	public static BuildListener listener=null;
	
	public static JsonResponse QueryAPI(String login, String password, String query)
	{
    	ClientConfig clientConfig = new DefaultClientConfig();
        
        Client client = Client.create(clientConfig);
        
        client.addFilter(new HTTPBasicAuthFilter(login, password));

		if (debugMode && listener!=null) listener.getLogger().println(String.format("QueryAPI: %s", query));
        WebResource webResource = client.resource(query);
        
        ClientResponse response = webResource.accept("application/json")
                .type("application/json").get(ClientResponse.class);
        
        return new JsonResponse(response.getStatus(), response.getEntity(String.class));	
	}
	
	public static int refreshAAD(String webServiceAddress, String login, String password)
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
				String.format("%s/rest/reload", webServiceAddress));		
        
		return jsonResponse.getCode();	
	}
	
	public static int listAads(String webServiceAddress, String login, String password, Collection<Aad> aads)
	{
    	
		JsonResponse jsonResponse = QueryAPI(login, password, 
				String.format("%s/rest/", webServiceAddress));		
        
        if (jsonResponse.getCode() == 200) {            
            Gson gson = new Gson();   
            
            Type collectionType = new TypeToken<Collection<Aad>>(){}.getType();
            Collection<Aad> tmp = gson.fromJson(jsonResponse.getJsonString(), collectionType);
            aads.addAll(tmp);
        } 
		return jsonResponse.getCode();
	}
	
	public static int listApplications(String webServiceAddress, String login, String password, String aad, 
										Collection<Application> apps)
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
				String.format("%s/rest/%s/applications", webServiceAddress, aad));		
        
        if (jsonResponse.getCode() == 200) {
            Gson gson = new Gson();   
            
            Type collectionType = new TypeToken<Collection<Application>>(){}.getType();
            Collection<Application> tmp = gson.fromJson(jsonResponse.getJsonString(), collectionType);
            apps.addAll(tmp);
        }  
		return jsonResponse.getCode();
	}	
	
	public static int listModules(String webServiceAddress, String login, String password, String aad, 
			int appId, Collection<Module> modules)
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
		String.format("%s/rest/%s/applications/%d/modules", webServiceAddress, aad, appId));
		
		if (jsonResponse.getCode() == 200) {
		Gson gson = new Gson();   
		
		Type collectionType = new TypeToken<Collection<Module>>(){}.getType();
		Collection<Module> tmp = gson.fromJson(jsonResponse.getJsonString(), collectionType);
		modules.addAll(tmp);
		}  
		return jsonResponse.getCode();
	}	
	
	public static int testConnection(String webServiceAddress, String login, String password)
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
				String.format("%s/rest/", webServiceAddress));
        
        if (jsonResponse.getCode() == 200)
        {
            Gson gson = new Gson();   
            
            Type collectionType = new TypeToken<Collection<Aad>>(){}.getType();
            try {
            	gson.fromJson(jsonResponse.getJsonString(), collectionType);
            } catch(JsonParseException e) {
            	return -200;
            }
        }        
        return jsonResponse.getCode();		
	}

	public static double getMetric(String webServiceAddress, String login, String password, String aad,
			int appId, int snapshotId, String metricGroup, int metricId, String metricField) throws RestException
	{
		double rslt = 4.0;
		String queryString = String.format("%s/rest/%s/applications/%d/snapshots/%d/results?%s=(%d)", 
				webServiceAddress, aad, appId, snapshotId, metricGroup, metricId);
		JsonResponse jsonResponse = QueryAPI(login, password, queryString);	

		if (jsonResponse.getCode() == 200) 
		{
			Gson gson = new Gson();   
		
			Type collectionType = new TypeToken<Collection<Metric>>(){}.getType();
			Collection<Metric> metrics = gson.fromJson(jsonResponse.getJsonString(), collectionType);
			
			if (metrics.size() == 1)
			{
				if (metrics.iterator().next().getApplicationResults().size() == 1)
				{
					if (metricField.equals("grade"))
						rslt = metrics.iterator().next().getApplicationResults().iterator().next().getResult().getGrade();
					else if (metricField.equals("value"))
						rslt = metrics.iterator().next().getApplicationResults().iterator().next().getResult().getValue();
//					else
//						throw new RestException(String.format("Unsupported data field (%s)!\n%s", metricField, queryString));
				}
//				else
//					throw new RestException(String.format("Unexpected number of results (%d)!\n%s", metrics.iterator().next().applicationResults.size(), queryString));
			}
//			else
//			{
//				throw new RestException(String.format("Unexpected number of results (%d)!\n%s", metrics.size(), queryString));
//			}
		}  
//		else
//		{
//			throw new RestException(String.format("Unable to access Rest API. Return Code: %d", jsonResponse.getCode()));
//		}	
		
		return rslt;
	}	
	
	public static int listSnapshots(String webServiceAddress, String login, String password, String aad,
			int appId, Collection<Snapshot> snapshots)
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
		String.format("%s/rest/%s/applications/%d/snapshots", 
				webServiceAddress, aad, appId));		
		
		if (jsonResponse.getCode() == 200) 
		{
			Gson gson = new Gson();   
		
			Type collectionType = new TypeToken<Collection<Snapshot>>(){}.getType();
			Collection<Snapshot> tmp = gson.fromJson(jsonResponse.getJsonString(), collectionType);
            snapshots.addAll(tmp);
		}  
		return 	jsonResponse.getCode();
	}
	
	private static int queryMetrics(String webServiceAddress, String login, String password, String aad,
			int appId, Collection<Metric> snapshots, String scope, boolean withEvolution)
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
		String.format("%s/rest/%s/applications/%d/results/?quality-indicators=%s%s", 
				webServiceAddress, aad, appId, scope, withEvolution ? "&select=evolutionSummary" : ""));		
		
		if (jsonResponse.getCode() == 200) 
		{
			Gson gson = new Gson();   
		
			Type collectionType = new TypeToken<Collection<Metric>>(){}.getType();
			Collection<Metric> tmp = gson.fromJson(jsonResponse.getJsonString(), collectionType);
            snapshots.addAll(tmp);
		}  
		return 	jsonResponse.getCode();
	}
	
	public static int listMetricsForApp(String webServiceAddress, String login, String password, String aad,
			int appId, Collection<Metric> snapshots)
	{
		return queryMetrics(webServiceAddress, login, password, aad, appId, snapshots, "(quality-rules)", false);	
	}
	
	public static int getMetricForApp(String webServiceAddress, String login, String password, String aad,
			int appId, int metricId, boolean withEvolution, Collection<Metric> snapshots)
	{
		return queryMetrics(webServiceAddress, login, password, aad, appId, snapshots, Integer.toString(metricId), withEvolution);	
	}
	
	public static int listMetrics(String webServiceAddress, String login, String password, String aad,
			Collection<Metric> snapshots)
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
		String.format("%s/rest/%s/results/?quality-indicators=(quality-rules)", 
				webServiceAddress, aad));		
		
		if (jsonResponse.getCode() == 200) 
		{
			Gson gson = new Gson();   
		
			Type collectionType = new TypeToken<Collection<Metric>>(){}.getType();
			Collection<Metric> tmp = gson.fromJson(jsonResponse.getJsonString(), collectionType);
            snapshots.addAll(tmp);
		}  
		return 	jsonResponse.getCode();
	}
	
	public static String getApplicationName(String webServiceAddress, String login, String password, String aad,
			int appId) throws RestException
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
				String.format("%s/rest/%s/applications/%d", 
						webServiceAddress, aad, appId));
		
		if (jsonResponse.getCode() == 200) 
		{
			Gson gson = new Gson(); 
			
			Application app = gson.fromJson(jsonResponse.getJsonString(), Application.class);
			
			return app.getName();
		}
		else
		{
			throw new RestException(String.format("Unable to access Rest API. Return Code: %d", jsonResponse.getCode()));
		}
	}
	
	public static int getApplicationId(String webServiceAddress, String login, String password, String aad, String appName)
	{
		Collection<Application> apps = new ArrayList<Application>();     
		
    	int responseStatus = listApplications(webServiceAddress, login, password, aad, apps);
    	
    	if (responseStatus !=  200)
    		return -1;
    	
    	for (Application app : apps) 
        {
        	if (app.getName().equals(appName) )
        		return Integer.parseInt(app.getHref().substring(app.getHref().lastIndexOf("/") + 1));
        }
		
		return -1;
	}	
	
	private static Snapshot getPenultimateSnapshot(String webServiceAddress, String login, String password, String aad,
			int appId) throws RestException
			{
				JsonResponse jsonResponse = QueryAPI(login, password, 
				String.format("%s/rest/%s/applications/%d/snapshots", 
						webServiceAddress, aad, appId));		
				
				if (jsonResponse.getCode() == 200) 
				{
					Gson gson = new Gson();   
				
					Type collectionType = new TypeToken<Collection<Snapshot>>(){}.getType();
					Collection<Snapshot> snapshots = gson.fromJson(jsonResponse.getJsonString(), collectionType);
										
					Collections.sort((List<Snapshot>) snapshots);
					
					if (snapshots.size() == 0)
						throw new RestException("This application has no snapshots.");
					
					if (snapshots.size() == 1)
						throw new RestException("This application has only 1 snapshot.");					
					
					return ((List<Snapshot>)snapshots).get(1);
				}  
				else
				{
					throw new RestException(String.format("Unable to access Rest API. Return Code: %d", jsonResponse.getCode()));
				}	
			}
			
	public static int getPenultimateSnapshotId(String webServiceAddress, String login, String password, String aad,
			int appId) throws RestException
	{
		return CastUtil.extractIdFromHref(getPenultimateSnapshot(webServiceAddress, login, password, aad, appId).href);
	}
	
	public static String getPenultimateSnapshotVersion(String webServiceAddress, String login, String password, String aad,
			int appId) throws RestException
	{
		return getPenultimateSnapshot(webServiceAddress, login, password, aad, appId).annotation.getVersion();
	}
	
	private static Snapshot getLastSnapshot(String webServiceAddress, String login, String password, String aad,
			int appId, boolean prodOnly) throws RestException
	{
		JsonResponse jsonResponse = QueryAPI(login, password, 
		String.format("%s/rest/%s/applications/%d/snapshots", 
				webServiceAddress, aad, appId));		
		
		if (jsonResponse.getCode() == 200) 
		{
			Gson gson = new Gson();   
		
			Type collectionType = new TypeToken<Collection<Snapshot>>(){}.getType();
			Collection<Snapshot> snapshots = gson.fromJson(jsonResponse.getJsonString(), collectionType);
			
			Snapshot latestSnapshot = null;
			for (Snapshot s : snapshots)
			{
				if (prodOnly && !s.getAnnotation().getVersion().startsWith("PRODVersion"))
				{
					continue;
				}
					
				if (latestSnapshot == null)
					latestSnapshot = s;
				else 
				{
					if (latestSnapshot.annotation.getDate().getTime() < s.annotation.getDate().getTime())
						latestSnapshot = s;
				}								
			}
			if (latestSnapshot == null && !prodOnly)
				throw new RestException("This application has no snapshots.");
			else
				return latestSnapshot;
		}  
		else
		{
			throw new RestException(String.format("Unable to access Rest API. Return Code: %d", jsonResponse.getCode()));
		}	
	}
			
	public static int getLastSnapshotId(String webServiceAddress, String login, String password, String aad,
			int appId, boolean prodOnly) throws RestException
	{
		Snapshot s = getLastSnapshot(webServiceAddress, login, password, aad, appId,prodOnly);
		if (s!=null) {
			return CastUtil.extractIdFromHref(s.href);
		} else {
			return 0;
		}
	}
	
	public static String getLastSnapshotVersion(String webServiceAddress, String login, String password, String aad,
			int appId, boolean prodOnly) throws RestException
	{
		Snapshot s = getLastSnapshot(webServiceAddress, login, password, aad, appId,prodOnly);
		if (s!=null) {
			return s.annotation.getVersion();
		} else {
			return "";
		}
	}
	
}
