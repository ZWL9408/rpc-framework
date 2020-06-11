package com.study.rpc.common.protocol;

import com.alibaba.fastjson.JSON;

public class JSONMessageProtocol implements MessageProtocol {
	public byte[] marshllingRequest(Request req) {
		Request temp = new Request();
		temp.setServiceName(req.getServiceName());
		temp.setMethod(req.getMethod());
		temp.setHeaders(req.getHeaders());
		temp.setPrameterTypes(req.getPrameterTypes());

		if(req.getParameters() != null){
			Object[] params = req.getParameters();
			Object[] serizeParmas =new Object[params.length];
			for (int i = 0; i < params.length; i++) {
				serizeParmas[i] = JSON.toJSONString(params[i]);
			}
			temp.setParameters(serizeParmas);
		}

		return new byte[0];
	}

	public Request unmarshallingRequest(byte[] data) throws Exception {
		Request req = JSON.parseObject(data, Request.class);
		if (req.getParameters() != null){

		}
		return null;
	}

	public byte[] marshallingResponse(Response rsp) throws Exception {
		return null;
	}

	public Response unmarshallingResponse(byte[] data) throws Exception {
		return null;
	}
}
