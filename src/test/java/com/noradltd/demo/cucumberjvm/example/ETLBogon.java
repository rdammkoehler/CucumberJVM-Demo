package com.noradltd.demo.cucumberjvm.example;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

public class ETLBogon extends Thread {

	enum State {
		READY, RUN, STOP
	}

	private State state = State.READY;
	private OrdersODS ods;

	private WatchService watcher = null;
	private static final Path dir = Paths.get(".");

	public ETLBogon(OrdersODS ods) throws IOException {
		this.ods = ods;
		watcher = FileSystems.getDefault().newWatchService();
		dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
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
		WatchKey key = poll();
		if (key != null) {
			processEvents(key);
			reset(key);
		}
	}

	private WatchKey poll() {
		WatchKey key = null;
		try {
			key = watcher.poll(1, TimeUnit.SECONDS);
		} catch (InterruptedException ie) {
			//bury
		}
		return key;
	}

	private void processEvents(WatchKey key) {
		for (WatchEvent<?> event : key.pollEvents()) {
			if (!isOverflow(event)) {
				processEvent(event);
			}
		}
	}
	
	private void processEvent(WatchEvent<?> event) {
		Path filename = getFilename(event);
		triggerOrdersODSLoader(filename);
		notifyWaiters();
	}

	private void triggerOrdersODSLoader(Path filename) {
		new OrdersODSLoader(ods).load(filename);
	}

	private boolean isOverflow(WatchEvent<?> event) {
		Kind<?> kind = event.kind();
		return (kind == OVERFLOW);
	}

	private Path getFilename(WatchEvent<?> event) {
		WatchEvent<Path> ev = cast(event);
		Path name = ev.context();
		Path filename = dir.resolve(name);
		return filename;
	}

	private void reset(WatchKey key) {
		if (!key.reset()) {
			System.err.println("KEY IS NOT ACTIVE, reset failed");
		}
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
	
	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

}
