package com.noradltd.demo.cucumberjvm.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
	
	private static final String COMMA = ",";

	private Long orderNumber;
	private Date orderDate;
	
	public Order(String orderNum, Date date) {
		orderNumber = new Long(orderNum);
		orderDate = date;
	}

	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return new StringBuilder().append(orderNumber).append(COMMA).append(dateFormat.format(orderDate)).toString();
	}
}
