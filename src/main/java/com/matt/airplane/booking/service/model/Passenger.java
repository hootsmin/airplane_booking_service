package com.matt.airplane.booking.service.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.matt.airplane.booking.service.exception.PassengerException;

public class Passenger implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private int passengerId;
	private boolean hasWindowPreference;
	private boolean isHappy;

	public Passenger(String passengerString) throws PassengerException {

		if (passengerString.endsWith("W")) {
			this.hasWindowPreference = true;
		} else {
			this.hasWindowPreference = false;
		}

		String passengerIdString = StringUtils.removeEnd(passengerString, "W");

		try {
			this.passengerId = Integer.parseInt(passengerIdString);
		} catch (NumberFormatException nfe) {
			throw new PassengerException("Passenger string illegally formatted: " + passengerString);
		}
		this.isHappy = false;
	}
	
	public Passenger() {
	}

	public int getPassengerId() {
		return passengerId;
	}

	public boolean hasWindowPreference() {
		return hasWindowPreference;
	}
	
	public boolean isHappy() {
		return isHappy;
	}

	public void setHappy(boolean isHappy) {
		this.isHappy = isHappy;
	}

	@Override
	public String toString() {
		return "Passenger [passengerId=" + passengerId + ", hasWindowPreference=" + hasWindowPreference + ", isHappy="
				+ isHappy + "]";
	}

}
