package com.noradltd.demo.cucumberjvm.example;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.PendingException;

public class ETLExampleStepDefs {

	private static final String ORDERS_CSV = "orders.csv";
	private OrdersOutputStreamWriter ordersWriter = new OrdersOutputStreamWriter();
	private List<Order> orders = new ArrayList<Order>();
	private OutputStream ordersOutputStream = new ByteArrayOutputStream();

	@Given("^a nightly orders load file$")
	public void a_nightly_orders_load_file() throws Throwable {
		/*
		 * Orders File Structure Order#, Order Date (YYYY-MM-DD)
		 */
		String[][] ordersData = new String[][] { { "1", "2013-10-23" }, { "2", "2013-10-23" } };
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (String[] orderStrings : ordersData) {
			orders.add(new Order(orderStrings[0], dateFormat.parse(orderStrings[1])));
		}
		ordersWriter.write(orders, ordersOutputStream);
	}

	@When("^the file arrives on the landing area$")
	public void the_file_arrives_on_the_landing_area() throws Throwable {
		FileWriter writer = null;
		try {
			writer = new FileWriter(ORDERS_CSV);
			writer.write(ordersOutputStream.toString());
			writer.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			writer.close();
		}
	}

	@Then("^Nightly Orders are loaded into the ODS$")
	public void Nightly_Orders_are_loaded_into_the_ODS() throws Throwable {
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Given("^an empty nightly orders load file$")
	public void an_empty_nightly_orders_load_file() throws Throwable {
		orders.clear();
	}

	@Then("^no changes occur in the ODS$")
	public void no_changes_occur_in_the_ODS() throws Throwable {
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Then("^an empty file notification is logged$")
	public void an_empty_file_notification_is_logged() throws Throwable {
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Given("^a corrupt nightly orders load file$")
	public void a_corrupt_nightly_orders_load_file() throws Throwable {
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}

	@Then("^a corrupt file notification is logged$")
	public void a_corrupt_file_notification_is_logged() throws Throwable {
		// Express the Regexp above with the code you wish you had
		throw new PendingException();
	}
}
