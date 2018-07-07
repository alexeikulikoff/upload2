package com.mibs.upload2.mars.service;

public class ExplorationUniqueName implements ExplorationNameProvider {
	
	private String uniqueName;
	public ExplorationUniqueName() {
		uniqueName = ExplorationName();
	}
	public String getUniqueName() {
		return uniqueName;
	}
}
