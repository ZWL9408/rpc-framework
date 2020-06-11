package com.study.rpc.server;

import com.study.rpc.common.protocol.MessageProtocol;
import com.study.rpc.common.protocol.Request;
import com.study.rpc.common.protocol.Response;
import com.study.rpc.common.protocol.Status;
import com.study.rpc.server.register.ServiceObject;
import com.study.rpc.server.register.ServiceRegister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.SocketHandler;

public class RequestHandler {

	private MessageProtocol protocol;

	private ServiceRegister serviceRegister;

	public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
		super();
		this.protocol = protocol;
		this.serviceRegister = serviceRegister;
	}

	public byte[] handleReqest(byte[] data) throws Exception{
		Request req = this.protocol.unmarshallingRequest(data);

		ServiceObject so = this.serviceRegister.getServiceObject(req.getServiceName());

		Response rsp = null;

		if(so == null) {
			rsp = new Response(Status.NOT_FOUND);
		}else {
			try{
				Method m = so.getInterf().getMethod(req.getMethod(), req.getPrameterTypes());
				Object returnValue = m.invoke(so.getObj(), req.getParameters());
				rsp = new Response(Status.SUCCESS);
				rsp.setReturnValue(returnValue);
			}catch (NoSuchMethodException | SecurityException |IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
				rsp = new Response(Status.ERROR);
				rsp.setException(e);
			}
		}
		return this.protocol.marshallingResponse(rsp);
	}

	public MessageProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(MessageProtocol protocol) {
		this.protocol = protocol;
	}

	public ServiceRegister getServiceRegister() {
		return serviceRegister;
	}

	public void setServiceRegister(ServiceRegister serviceRegister) {
		this.serviceRegister = serviceRegister;
	}
}
