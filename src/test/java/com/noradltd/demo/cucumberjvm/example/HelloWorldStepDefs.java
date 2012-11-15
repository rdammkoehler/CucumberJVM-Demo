package com.noradltd.demo.cucumberjvm.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HelloWorldStepDefs {

	private HelloWorld helloWorld;
	private String hi;

	@Given("^I have a hello app with \"([^\"]*)\"$")
	public void I_have_a_hello_app_with(String greeting) {
		helloWorld = new HelloWorld(greeting);
	}

	@When("^I ask it to say hi$")
	public void I_ask_it_to_say_hi() {
		hi = helloWorld.sayHi();
	}

	@Then("^it should answer with \"([^\"]*)\"$")
	public void it_should_answer_with(String expectedHi) {
		assertThat(hi, is(expectedHi));
	}

}
