package com.study.rpc.demo.provider;

import com.study.rpc.common.protocol.JavaSerializeMessageProtocol;
import com.study.rpc.common.protocol.Request;
import com.study.rpc.demo.DemoService;
import com.study.rpc.server.NettyRpcServer;
import com.study.rpc.server.RequestHandler;
import com.study.rpc.server.RpcServer;
import com.study.rpc.server.register.ServiceObject;
import com.study.rpc.server.register.ServiceRegister;
import com.study.rpc.server.register.ZookeeperExportServiceRegister;
import com.study.rpc.util.PropertiesUtils;

public class Provider {
	public static void main(String[] args) throws Exception {
		int port = Integer.parseInt(PropertiesUtils.getProperties("rpc.port"));
		String protocol = PropertiesUtils.getProperties("rpc.protocol");

		ServiceRegister sr = new ZookeeperExportServiceRegister();
		DemoService ds = new DemoServiceImpl();
		ServiceObject so = new ServiceObject(DemoService.class.getName(),DemoService.class,ds);
		sr.register(so, protocol, port);
		RequestHandler reqHandler = new RequestHandler(new JavaSerializeMessageProtocol(),sr);
		RpcServer server = new NettyRpcServer(port, protocol, reqHandler);
		server.start();
		System.in.read();
		server.stop();
	}
}
