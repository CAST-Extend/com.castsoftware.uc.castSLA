package com.castsoftware.restapi.pojo;

import java.util.Date;

public class JsonDate {
    private long time;
    
    public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Date getAsDate() {return new Date(time);}
}
