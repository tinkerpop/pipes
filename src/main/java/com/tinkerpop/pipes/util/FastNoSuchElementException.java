package com.tinkerpop.pipes.util;

import java.util.NoSuchElementException;

public class FastNoSuchElementException extends NoSuchElementException {

	private static final long serialVersionUID = 2303108654138257697L;
	private static final FastNoSuchElementException instance = new FastNoSuchElementException();

	/**
	 * Retrieve a singleton, fast {@link NoSuchElementException} without a stack trace.
	 */
	public static NoSuchElementException instance() {
		return instance;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

}
