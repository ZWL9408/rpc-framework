package com.study.rpc.client.net;

import com.study.rpc.discovery.ServiceInfo;

public interface NetClient {
	byte[] sendRequest(byte[] data, ServiceInfo sinfo) throws Throwable;
}
