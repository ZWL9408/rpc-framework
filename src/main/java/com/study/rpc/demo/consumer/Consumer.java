package com.study.rpc.demo.consumer;

import com.study.rpc.client.ClientStubProxyFactory;
import com.study.rpc.client.net.NettyNetClient;
import com.study.rpc.common.protocol.JavaSerializeMessageProtocol;
import com.study.rpc.common.protocol.MessageProtocol;
import com.study.rpc.demo.DemoService;
import com.study.rpc.discovery.ZookeeperServiceInfoDiscoverer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Consumer {
	public static void main(String[] args) {
		ClientStubProxyFactory cspf = new ClientStubProxyFactory();
		cspf.setSid(new ZookeeperServiceInfoDiscoverer());

		Map<String, MessageProtocol> supportMessageProtocols = new HashMap<String, MessageProtocol>();
		supportMessageProtocols.put("javas",new JavaSerializeMessageProtocol());
		cspf.setSupportMessageProtocols(supportMessageProtocols);

		cspf.setNetClient(new NettyNetClient());

		DemoService demoService = cspf.getProxy(DemoService.class);
		String hello = demoService.sayHello("world");
		System.out.println(hello);
		System.out.println(demoService.multiPoint(new Point(5,10),2));
	}
}
