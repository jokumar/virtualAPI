package com.geeks18.virtualserver.rule.dto;

public class APIDetails {
private String url;
private String methodType;
private String message;
private Boolean status;
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}
public String getMethodType() {
	return methodType;
}
public void setMethodType(String methodType) {
	this.methodType = methodType;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Boolean getStatus() {
	return status;
}
public void setStatus(Boolean status) {
	this.status = status;
}

}
