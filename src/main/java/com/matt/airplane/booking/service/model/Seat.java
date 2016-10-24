package com.matt.airplane.booking.service.model;

import java.io.Serializable;

public class Seat implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private Passenger passenger;
	private boolean isWindowSeat;
	private boolean isOccupied;

	public Seat(boolean isWindowSeat, boolean isOccupied) {
		this.isWindowSeat = isWindowSeat;
		this.isOccupied = isOccupied;
	}
	
	public Seat(Seat seat) {
		this.passenger = seat.passenger;
		this.isWindowSeat = seat.isWindowSeat;
		this.isOccupied = seat.isOccupied;
	}

	public boolean isWindowSeat() {
		return isWindowSeat;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public boolean setOccupied(Passenger passenger) {
		boolean successfullyOccupied = false;
		if (!this.isOccupied) {
			this.isOccupied = true;
			this.passenger = passenger;
			successfullyOccupied = true;
		}
		return successfullyOccupied;
	}

	public Passenger getPassenger() {
		return this.passenger;
	}
	
	public void removePassengerFromSeat() {
		this.passenger = null;
		this.isOccupied = false;
	}

	@Override
	public String toString() {
		return "Seat [passenger=" + passenger + ", isWindowSeat=" + isWindowSeat + ", isOccupied=" + isOccupied + "]";
	}

}
