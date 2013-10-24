package com.noradltd.demo.cucumberjvm.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ETLExampleStepDefs {

	private static final String ORDERS_CSV = "orders.csv";
	private OrdersOutputStreamWriter ordersWriter = new OrdersOutputStreamWriter();
	private List<Order> orders = new ArrayList<Order>();
	private OutputStream ordersOutputStream = new ByteArrayOutputStream();
	private OrdersODS ordersODS = new OrdersODS();
	private ETLBogon etlBogon = null;

	@Before
	public void beforeETLExampleFeature() {
		if ( etlBogon == null ) {
			etlBogon = new ETLBogon(ordersODS);
			etlBogon.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@After
	public void afterELTExampleFeature() {
		if ( etlBogon != null ) {
			try {
				etlBogon.quit().join(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			etlBogon = null;
		}
		new File(ORDERS_CSV).delete();
	}
	
	@Given("^a nightly orders load file$")
	public void a_nightly_orders_load_file() throws Throwable {
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
		writer = null;
		synchronized (etlBogon) {			
			etlBogon.wait(20000);
		}
	}

	@Then("^Nightly Orders are loaded into the ODS$")
	public void Nightly_Orders_are_loaded_into_the_ODS() throws Throwable {
		for(Order order : orders) {
			List<Order> odsMatches = ordersODS.find(order);
			assertThat(odsMatches.size(), is(1));
			assertThat(odsMatches.get(0), is(order));
		}
	}

	@Given("^an empty nightly orders load file$")
	public void an_empty_nightly_orders_load_file() throws Throwable {
		orders.clear();
	}

	@Then("^no changes occur in the ODS$")
	public void no_changes_occur_in_the_ODS() throws Throwable {
		assertThat(ordersODS.size(), is(0)); //not really valid, work it out
	}

	@Then("^an empty file notification is logged$")
	public void an_empty_file_notification_is_logged() throws Throwable {
		assertThat(ordersODS.log().pop(), is("Empty File"));
	}

	@Given("^a corrupt nightly orders load file$")
	public void a_corrupt_nightly_orders_load_file() throws Throwable {
		ordersOutputStream.write("This file is invalid".getBytes());
	}

	@Then("^a corrupt file notification is logged$")
	public void a_corrupt_file_notification_is_logged() throws Throwable {
		assertThat(ordersODS.log().pop(), is("Corrupt Input File"));
	}
}
