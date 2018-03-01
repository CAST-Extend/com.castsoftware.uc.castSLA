package com.castsoftware.restapi.pojo;

import java.util.Date;

public class JsonDate {
    public long time;
    
    public Date getAsDate() {return new Date(time);}
}
