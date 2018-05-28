package com.xianjinxia.cashman.exceptions;

public class SqlUpdateException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SqlUpdateException(String message) {
        super(message);
    }

    public SqlUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlUpdateException(Throwable cause) {
        super(cause);
    }
}
