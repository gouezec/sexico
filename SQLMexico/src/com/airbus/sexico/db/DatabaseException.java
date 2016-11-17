package com.airbus.sexico.db;

public class DatabaseException extends Exception {

	private static final long serialVersionUID = 1L;

	private int reason;

	public final static int REASON_UNKNOWN = 0;
	public final static int REASON_UNIQUE_ID_VIOLATION = 1;
	public final static int REASON_INVALID_DATABASE = 2;
	public final static int REASON_UNKNOW_DRIVER = 3;
	
	public final int getReason() {
		return reason;
	}

	public DatabaseException(String arg0, int reason) {
		super(arg0);
		this.reason = REASON_UNKNOWN;
	}

	public DatabaseException(Throwable arg0, int reason) {
		super(arg0);
		this.reason = reason;
	}

	public DatabaseException(String arg0, Throwable arg1, int reason) {
		super(arg0, arg1);
	}



}
