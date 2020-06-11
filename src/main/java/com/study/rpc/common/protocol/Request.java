package com.study.rpc.common.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable{
	private static final long serialVersionUID = -5200571424236772650L;

	private String serviceName;
	private String method;
	private Map<String,String> headers = new HashMap<String, String>();
	private Class<?>[] prameterTypes;
	private Object[] parameters;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Class<?>[] getPrameterTypes() {
		return prameterTypes;
	}

	public void setPrameterTypes(Class<?>[] prameterTypes) {
		this.prameterTypes = prameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public String getHeader(String name){
		return this.headers == null ? null : this.headers.get(name);
	}
}
