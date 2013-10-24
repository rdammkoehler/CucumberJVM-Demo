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

	public ETLBogon(OrdersODS ods) {
		this.ods = ods;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	@Override
	public void run() {
		state = State.RUN;
		while (state == State.RUN) {
			WatchKey key = null;
			try {
				key = watcher.poll(1, TimeUnit.SECONDS);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			if (key == null) {
				continue;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				Kind<?> kind = event.kind();

				if (kind == OVERFLOW) {
					System.out.println("OVERFLOW!");
					continue;
				}

				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path filename = dir.resolve(name);

				new OrdersODSLoader(ods).load(filename);

				synchronized (this) {
					notifyAll();
				}
			}
			if ( ! key.reset() ) {
				System.err.println("KEY IS NOT ACTIVE, reset failed");
			}
		}
		synchronized (this) {
			notifyAll();
		}
	}

	public ETLBogon quit() {
		state = State.STOP;
		return this;
	}
}
