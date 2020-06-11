package com.study.rpc.discovery;

import java.util.List;

public interface ServiceInfoDiscoverer {
	List<ServiceInfo> getServiceInfo(String name);
}
