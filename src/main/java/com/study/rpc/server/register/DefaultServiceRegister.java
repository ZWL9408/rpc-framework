package com.study.rpc.server.register;

import java.util.HashMap;
import java.util.Map;

public class DefaultServiceRegister implements ServiceRegister{

	private Map<String, ServiceObject> serviceMap = new HashMap<String, ServiceObject>();

//	@Override
	public void register(ServiceObject so, String protocolName, int port) throws Exception {
		if (so == null) {
			throw new IllegalAccessException("parameter can't be null");
		}
		this.serviceMap.put(so.getName(),so);
	}
//	@Override
	public ServiceObject getServiceObject(String name) throws Exception {
		return this.serviceMap.get(name);
	}
}
