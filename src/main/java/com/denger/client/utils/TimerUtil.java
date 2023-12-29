package com.denger.client.utils;

public class TimerUtil {
	public long mc = System.currentTimeMillis();

	public void reset() {
		this.mc = System.currentTimeMillis();
	}

	public long getMc() {
		return System.currentTimeMillis() - this.mc;
	}

	public boolean hasReached(long n) {
		return System.currentTimeMillis() - this.mc > n;
	}
}
