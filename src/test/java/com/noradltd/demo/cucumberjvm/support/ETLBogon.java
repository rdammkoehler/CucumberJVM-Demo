package com.noradltd.demo.cucumberjvm.support;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.noradltd.demo.cucumberjvm.ordersods.OrdersODS;
import com.noradltd.demo.cucumberjvm.ordersods.OrdersODSLoader;

public class ETLBogon extends Thread {

  private static final Path dir = Paths.get(".");

  enum State {
    READY, RUN, STOP
  }

  private State state = State.READY;
  private OrdersODS ods;
  private Object semaphore;

  public ETLBogon(OrdersODS ordersODS, Object semaphore) {
    this.ods = ordersODS;
    this.semaphore = semaphore;
  }

  @Override
  public void run() {
    state = State.RUN;
    while (state == State.RUN) {
      waitForFlatFile();
    }
    notifyWaiters();
  }

  private void waitForFlatFile() {
    synchronized (semaphore) {
      try {
        semaphore.wait();
      } catch (InterruptedException e) {
        // bury
      }
    }
    List<String> csvFiles = Arrays.asList(dir.toFile().list(new FilenameFilter() {

      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith("csv");
      }
    }));
    if (! csvFiles.isEmpty()) {
      triggerOrdersODSLoader(dir.resolve(csvFiles.get(0)));
      quit();
    }
  }

  private void triggerOrdersODSLoader(Path filename) {
    new OrdersODSLoader(ods).load(filename);
  }

  private void notifyWaiters() {
    synchronized (this) {
      notifyAll();
    }
  }

  public ETLBogon quit() {
    state = State.STOP;
    return this;
  }

}
