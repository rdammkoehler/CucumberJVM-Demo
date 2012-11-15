package com.noradltd.demo.cucumberjvm.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StringReverserStepDefs {

	private StringReverser stringReverser;
	private String result;
	private List<String> results;

	@Given("^a String Reverser$")
	public void a_String_Reverser() throws Throwable {
		stringReverser = new StringReverser();
	}

	@When("^I reverse the string \"([^\"]*)\"$")
	public void I_reverse_the_string(String input) throws Throwable {
		result = stringReverser.reverse(input);
	}

	@Then("^the result is \"([^\"]*)\"$")
	public void the_result_is(String expected) throws Throwable {
		assertThat(result, is(expected));
	}

	@When("^I reverse these strings:$")
	public void I_reverse_these_strings(List<String> inputs) throws Throwable {
		results = new ArrayList<String>();
		for(String input : inputs) {
			results.add(stringReverser.reverse(input));
		}
	}

	@Then("^the results are:$")
	public void the_results_are(List<String> expecteds) throws Throwable {
		Iterator<String> resultItr = results.iterator();
		for(String expected : expecteds) {
			assertThat(resultItr.next(), is(expected));
		}
	}
}
