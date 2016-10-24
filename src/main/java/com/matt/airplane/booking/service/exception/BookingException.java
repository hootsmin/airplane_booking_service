package com.matt.airplane.booking.service.exception;

public class BookingException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	  
	  public BookingException(String message) {
	    super(message);
	  }
	  
	  public BookingException(String message, Throwable t) {
	    super(message, t);
	  }
	
}
