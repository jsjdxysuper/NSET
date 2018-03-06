package com.kedong.nset.exception;

public class NoEnoughSample extends Exception{

	public NoEnoughSample() {
		super();
	}

	public NoEnoughSample(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoEnoughSample(String message, Throwable cause) {
		super(message, cause);
	}

	public NoEnoughSample(String message) {
		super(message);
	}

	public NoEnoughSample(Throwable cause) {
		super(cause);
	}

}
