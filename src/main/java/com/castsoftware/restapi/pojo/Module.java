package com.castsoftware.restapi.pojo;

import java.util.List;

public class Module {
    public String href;
    public String name;
    public List<String> technologies;
    public SimpleReference applications;
    public SimpleReference snapshots;
    public SimpleReference results;
}
