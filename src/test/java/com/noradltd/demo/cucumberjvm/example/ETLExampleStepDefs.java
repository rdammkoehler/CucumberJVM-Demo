package com.noradltd.demo.cucumberjvm.example;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
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

  private List<Order> orders = new ArrayList<Order>();
  private OutputStream ordersOutputStream = new ByteArrayOutputStream();
  private OrdersODS ordersODS = new OrdersODS();
  private ETLBogon etlBogon = null;

  @Before
  public void beforeETLExampleFeature() throws IOException {
    startETLProcess();
  }

  @After
  public void afterELTExampleFeature() {
    stopETLProcess();
  }

  @Given("^a nightly orders load file$")
  public void a_nightly_orders_load_file() throws Throwable {
    loadValidOrders();
  }

  @When("^the file arrives on the landing area$")
  public void the_file_arrives_on_the_landing_area() throws Throwable {
    writeOrdersToFile();
    waitForETLProcess();
  }

  @Then("^Nightly Orders are loaded into the ODS$")
  public void Nightly_Orders_are_loaded_into_the_ODS() throws Throwable {
    for (Order order : orders) {
      assertThat(ordersODS.find(order), contains(order));
    }
  }

  @Given("^an empty nightly orders load file$")
  public void an_empty_nightly_orders_load_file() throws Throwable {
    orders.clear();
  }

  @Then("^no changes occur in the ODS$")
  public void no_changes_occur_in_the_ODS() throws Throwable {
    assertThat(ordersODS.size(), is(0)); // not really valid, work it out
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

  @Given("^a partially corrupt nightly orders load file$")
  public void a_partially_corrupt_nightly_orders_load_file() throws Throwable {
    loadValidOrders();
    ordersOutputStream.write("This file is invalid".getBytes());
  }

  private void loadValidOrders() throws ParseException {
    String[][] ordersData = new String[][] { { "1", "2013-10-23" }, { "2", "2013-10-23" } };
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    for (String[] orderStrings : ordersData) {
      orders.add(new Order(orderStrings[0], dateFormat.parse(orderStrings[1])));
    }
    new OrdersOutputStreamWriter().write(orders, ordersOutputStream);
  }

  private void writeOrdersToFile() throws IOException {
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
    synchronized (ordersOutputStream) {
      ordersOutputStream.notifyAll();
    }
  }

  private void startETLProcess() throws IOException {
    if (etlBogon == null) {
      etlBogon = new ETLBogon(ordersODS, ordersOutputStream);
      etlBogon.start();
    }
  }

  private void stopETLProcess() {
    if (etlBogon != null) {
      try {
        etlBogon.quit().join(10000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      etlBogon = null;
    }
  }

  private void waitForETLProcess() throws InterruptedException {
    synchronized (etlBogon) {
      etlBogon.wait(20000);
    }
  }

}
