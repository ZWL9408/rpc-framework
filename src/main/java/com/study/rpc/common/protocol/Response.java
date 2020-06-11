package com.study.rpc.common.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {

	private static final long serialVersionUID = -4317845782629589997L;
	private Status status;
	private Map<String, String> headers = new HashMap<String, String>();
	private Object returnValue;
	private Exception exception;
	public Response(){};
	public Response(Status status){
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getHeder(String name){
		return this.headers == null ? null : this.headers.get(name);
	}
	public  void setHaader(String name, String value){
		this.headers.put(name,value);
	}

}
