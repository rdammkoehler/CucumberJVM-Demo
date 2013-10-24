package com.noradltd.demo.cucumberjvm.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ODSLoader {

	private OrdersODS ordersODS;

	public ODSLoader(OrdersODS ods) {
		ordersODS = ods;
	}

	//There is a hole here, a partially corrupt file will modify the ods
	public void load(Path filename) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename.toFile()));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			long lineCount = 0;
			while (reader.ready()) {
				String data = reader.readLine();
				String[] orderStrings = data.split(",");
				ordersODS.add(new Order(orderStrings[0], dateFormat.parse(orderStrings[1])));
				lineCount++;
			}
			if ( lineCount == 0 ) { 
				ordersODS.log("Empty File");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ordersODS.log("Corrupt Input File");
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
