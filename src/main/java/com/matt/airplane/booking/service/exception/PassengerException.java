package com.matt.airplane.booking.service.exception;

public class PassengerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	  
	  public PassengerException(String message) {
	    super(message);
	  }
	  
	  public PassengerException(String message, Throwable t) {
	    super(message, t);
	  }
}
