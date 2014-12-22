package com.googlecode.objectify.remotely;

import lombok.RequiredArgsConstructor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 */
@RequiredArgsConstructor
public class Now<T> implements Future<T> {
	private final T thing;

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		return thing;
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return thing;
	}
}
