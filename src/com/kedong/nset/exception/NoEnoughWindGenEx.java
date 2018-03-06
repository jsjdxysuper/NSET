package com.kedong.nset.exception;

public class NoEnoughWindGenEx extends Exception{

	public NoEnoughWindGenEx() {
		super();
	}

	public NoEnoughWindGenEx(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoEnoughWindGenEx(String message, Throwable cause) {
		super(message, cause);
	}

	public NoEnoughWindGenEx(String message) {
		super(message);
	}

	public NoEnoughWindGenEx(Throwable cause) {
		super(cause);
	}

}
