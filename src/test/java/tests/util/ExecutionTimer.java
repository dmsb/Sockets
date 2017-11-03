package tests.util;

import java.util.concurrent.TimeUnit;

public class ExecutionTimer {

	public static long time(Runnable r) {
		long start = System.nanoTime();
		r.run();
		long t = System.nanoTime() - start;
		return TimeUnit.MILLISECONDS.convert(t, TimeUnit.NANOSECONDS);		
	}
}
