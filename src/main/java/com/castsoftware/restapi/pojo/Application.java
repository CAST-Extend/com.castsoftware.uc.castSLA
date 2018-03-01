package com.castsoftware.restapi.pojo;

import java.util.List;

public class Application {
    public String href;
    public String name;
    public List<String> technologies;
    public SimpleReference systems;
    public SimpleReference modules;
    public SimpleReference snapshots;
    public SimpleReference results;
    public String origin;
    public String adgDatabase;
    public String adgWebSite;
    public String adgLocalId;
    public String adgVersion;
}
