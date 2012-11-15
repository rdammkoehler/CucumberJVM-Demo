package com.noradltd.demo.cucumberjvm.example;

public class HelloWorld {

	private final String greeting;
	
	public HelloWorld(String greeting) {
		this.greeting = greeting;
	}

	public String sayHi() {
		return greeting + " World";
	}

}
