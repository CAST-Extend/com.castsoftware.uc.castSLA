package com.castsoftware.util;

public class VersionInfo {
	public String version;
	public int build;
	
	public VersionInfo()
	{
		
	}
	
	public VersionInfo(String version, int build)
	{
		this.version = version;
		this.build = build;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s build %d", version, build);
	}
}
