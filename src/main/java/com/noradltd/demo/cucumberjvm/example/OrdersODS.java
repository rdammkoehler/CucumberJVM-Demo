package com.noradltd.demo.cucumberjvm.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

public class OrdersODS {

	private List<Order> orders = new ArrayList<Order>();
	private Stack<String> log = new Stack<String>();

	public List<Order> find(Order order) {
		return Lists.newArrayList(Collections2.filter(orders, Predicates.equalTo(order)));
	}

	public boolean add(Order order) {
		return orders.add(order);
	}

	public Integer size() {
		return orders.size();
	}

	public Stack<String> log() {	
		return log;
	}
	
	public void log(String message) {
		log.push(message);
	}

}
