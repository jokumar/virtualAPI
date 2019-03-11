package com.geeks18.virtualserver.drools.model;

public class GenericPojoModel {

	private String key;
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	@Override
	public String toString() {
		return "GenericPojoModel [key=" + key + ", value=" + value + "]";
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
