package com.study.rpc.server.register;

import com.alibaba.fastjson.JSON;
import com.study.rpc.discovery.ServiceInfo;
import com.study.rpc.util.PropertiesUtils;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

public class ZookeeperExportServiceRegister extends DefaultServiceRegister implements ServiceRegister {

	private ZkClient client;

	private String centerRootPath = "/Rpc-framework";

	public ZookeeperExportServiceRegister(){
		String addr = PropertiesUtils.getProperties("zk.address");
		client = new ZkClient(addr);
		client.setZkSerializer(new MyZkSerializer());
	}

	@Override
	public void register(ServiceObject so, String protocolName, int port) throws Exception {
		super.register(so, protocolName, port);
		ServiceInfo soInf = new ServiceInfo();

		String host = InetAddress.getLocalHost().getHostAddress();
		String address = host + ":" + port;
		soInf.setAddress(address);
		soInf.setName(so.getInterf().getName());
		soInf.setProtocol(protocolName);
		this.exportService(soInf);
	}

	private void exportService(ServiceInfo serviceResource){
		String serviceName = serviceResource.getName();
		String uri = JSON.toJSONString(serviceResource);
		try {
			uri = URLEncoder.encode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		String servicePath = centerRootPath + "/" + serviceName + "/service";
		if (!client.exists(servicePath)){
			client.createPersistent(servicePath,true);
		}
		String uriPath = servicePath + "/" + uri;
		if(client.exists(uriPath)){
			client.delete(uriPath);
		}
		client.createEphemeral(uriPath);
	}
}
