package com.study.rpc.demo.provider;

import com.study.rpc.demo.DemoService;

import java.awt.*;

public class DemoServiceImpl implements DemoService{
	public String sayHello(String name) {
		return "Hello" + name;
	}

	public Point multiPoint(Point p, int multi) {
		p.x = p.x * multi;
		p.y = p.y * multi;
		return p;
	}
}
