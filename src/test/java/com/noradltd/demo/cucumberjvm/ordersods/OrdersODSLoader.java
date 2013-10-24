package com.noradltd.demo.cucumberjvm.ordersods;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

import com.noradltd.demo.cucumberjvm.ordersods.Order;
import com.noradltd.demo.cucumberjvm.ordersods.OrdersODS;

public class OrdersODSLoader {

  private OrdersODS ordersODS;

  public OrdersODSLoader(OrdersODS ods) {
    ordersODS = ods;
  }

  public void load(Path filename) {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(filename.toFile()));
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      long lineCount = 0;
      ordersODS.beginTxn();
      while (reader.ready()) {
        String data = reader.readLine();
        String[] orderStrings = data.split(",");
        ordersODS.add(new Order(orderStrings[0], dateFormat.parse(orderStrings[1])));
        lineCount++ ;
      }
      if (lineCount == 0) {
        ordersODS.log("Empty File");
        ordersODS.rollback();
      } else {
        ordersODS.commit();
      }
    } catch (Exception e) {
      ordersODS.log(e.getMessage());
      ordersODS.log("Corrupt Input File");
      ordersODS.rollback();
    } finally {
      try {
        reader.close();
      } catch (IOException e) {
        // bury
      }
      filename.toFile().delete();
    }
  }

}
