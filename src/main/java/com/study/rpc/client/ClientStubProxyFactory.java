package com.study.rpc.client;

import com.study.rpc.client.net.NetClient;
import com.study.rpc.common.protocol.MessageProtocol;
import com.study.rpc.common.protocol.Request;
import com.study.rpc.common.protocol.Response;
import com.study.rpc.discovery.ServiceInfo;
import com.study.rpc.discovery.ServiceInfoDiscoverer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ClientStubProxyFactory {

	private ServiceInfoDiscoverer sid;

	private Map<String, MessageProtocol> supportMessageProtocols;

	private NetClient netClient;

	private Map<Class<?>, Object> objectCache = new HashMap<Class<?>, Object>();

	public <T> T getProxy(Class<T> interf) {
		T obj = (T)this.objectCache.get(interf);
		if(obj == null){
			obj = (T) Proxy.newProxyInstance(interf.getClassLoader(), new Class<?>[]{interf},
					new ClientStubInvocationHandler(interf));
			this.objectCache.put(interf,obj);
		}
		return obj;
	}

	public ServiceInfoDiscoverer getSid() {
		return sid;
	}

	public void setSid(ServiceInfoDiscoverer sid) {
		this.sid = sid;
	}

	public Map<String, MessageProtocol> getSupportMessageProtocols() {
		return supportMessageProtocols;
	}

	public void setSupportMessageProtocols(Map<String, MessageProtocol> supportMessageProtocols) {
		this.supportMessageProtocols = supportMessageProtocols;
	}

	public NetClient getNetClient() {
		return netClient;
	}

	public void setNetClient(NetClient netClient) {
		this.netClient = netClient;
	}

	private class ClientStubInvocationHandler implements InvocationHandler{

		private Class<?>interf;

		private Random random = new Random();

		public ClientStubInvocationHandler(Class<?> interf){
			super();
			this.interf = interf;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("toString")){
				return proxy.getClass().toString();
			}
			if(method.getName().equals("hashCode")){
				return 0;
			}

			String serviceName = this.interf.getName();
			List<ServiceInfo> sinfos = sid.getServiceInfo(serviceName);

			if(sinfos == null || sinfos.size() == 0){
				throw new Exception("remote service doen't exits!");
			}

			ServiceInfo sinfo = sinfos.get(random.nextInt(sinfos.size()));

			Request req = new Request();
			req.setServiceName(sinfo.getName());
			req.setMethod(method.getName());
			req.setPrameterTypes(method.getParameterTypes());
			req.setParameters(args);

			MessageProtocol protocol = supportMessageProtocols.get(sinfo.getProtocol());

			byte[] data = protocol.marshllingRequest(req);
			byte[] repData = netClient.sendRequest(data, sinfo);

			Response rsp = protocol.unmarshallingResponse(repData);

			if (rsp.getException() != null){
				throw rsp.getException();
			}

			return rsp.getReturnValue();
		}
	}
}
