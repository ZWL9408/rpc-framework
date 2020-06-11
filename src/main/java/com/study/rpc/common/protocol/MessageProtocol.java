package com.study.rpc.common.protocol;


public interface MessageProtocol {
	byte[] marshllingRequest(Request req) throws Exception;
	Request unmarshallingRequest(byte[] data) throws Exception;
	byte[] marshallingResponse(Response rsp) throws  Exception;
	Response unmarshallingResponse(byte[] data) throws Exception;
}
