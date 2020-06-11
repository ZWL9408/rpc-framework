package com.study.rpc.common.protocol;

import sun.reflect.annotation.ExceptionProxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializeMessageProtocol implements MessageProtocol{
	private byte[] serialize(Object obj) throws Exception{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bout);
		out.writeObject(obj);
		return bout.toByteArray();
	}
	public byte[] marshllingRequest(Request req) throws Exception {
		return this.serialize(req);
	}

	public Request unmarshallingRequest(byte[] data) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
		return (Request)in.readObject();
	}

	public byte[] marshallingResponse(Response rsp) throws Exception {
		return this.serialize(rsp);
	}

	public Response unmarshallingResponse(byte[] data) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
		return (Response)in.readObject();
	}
}
