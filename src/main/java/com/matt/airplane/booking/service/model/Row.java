package com.matt.airplane.booking.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Row implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private Seat[] seats;
	private int seatsAvailable;
	private int nextAvailable = 0;

	public Row(int seatsInRow) {
		seats = new Seat[seatsInRow];

		for (int i = 0; i < seatsInRow; i++) {
			Seat seat = new Seat(isAWindowSeat(i, seatsInRow), false);
			seats[i] = seat;
		}
		this.seatsAvailable = seatsInRow;
	}
	
	public Row(Row row) {
		this.seatsAvailable = row.seatsAvailable;
		this.nextAvailable = row.nextAvailable;
		this.seats = new Seat[row.seats.length];
		for (int i=0; i<row.seats.length; i++) {
			this.seats[i] = row.seats[i];
		}
	}
	
	public boolean addPassengerToARow(Passenger passenger, boolean preferencesMet) {
		boolean passengerAdded = false;
		if (seatsAvailable > 0) {
			passenger.setHappy(preferencesMet);
			seats[nextAvailable].setOccupied(passenger);
			nextAvailable++;
			seatsAvailable--;
			passengerAdded = true;
		}
		return passengerAdded;
	}

	public boolean addGroupToRow(Group group) {
		boolean groupAdded = false;
		int groupSize = group.getPassengers().size();
		if (seatsAvailable - groupSize >= 0) {
			List<Passenger> passengers = new ArrayList<Passenger>(group.getPassengers());

			Iterator<Passenger> passengersWithNoPreferenceIter = getPassengersWithNoWindowSeatPreference(passengers)
					.iterator();
			Iterator<Passenger> passengersWithWindowSeatPreferenceIter = getPassengersWithWindowSeatPreference(
					passengers).iterator();

			while (passengersWithNoPreferenceIter.hasNext() || passengersWithWindowSeatPreferenceIter.hasNext()) {
				// Check if next seat is a window seat
				if (seats[nextAvailable].isWindowSeat()) {
					if (passengersWithWindowSeatPreferenceIter.hasNext()) {
						addPassengerToARow(passengersWithWindowSeatPreferenceIter.next(), true);
					} else {
						addPassengerToARow(passengersWithNoPreferenceIter.next(), true);
					}
				} else {
					if (passengersWithNoPreferenceIter.hasNext()) {
						addPassengerToARow(passengersWithNoPreferenceIter.next(), true);
					} else {
						addPassengerToARow(passengersWithWindowSeatPreferenceIter.next(), false);
					}
				}
			}
			groupAdded = true;
		}
		return groupAdded;
	}
	
	public List<Passenger> getPassengersAssignedToRow() {
		List<Passenger> passengers = new ArrayList<Passenger>();
		for (int i = 0; i < seats.length; i++) {
			if (seats[i].isOccupied()) {
				passengers.add(seats[i].getPassenger());
			}
		}
		return passengers;
	}
	
	public int getSeatsAvailable() {
		return seatsAvailable;
	}
	
	public void removeAllPassengersFromRow() {
		for (int i = 0; i < seats.length; i++) {
			seats[i].removePassengerFromSeat();
		}
		this.seatsAvailable = seats.length;
		this.nextAvailable = 0;
	}

	private List<Passenger> getPassengersWithWindowSeatPreference(List<Passenger> passengers) {
		List<Passenger> passengersWithWindowPreference = new ArrayList<Passenger>();
		for (Passenger passenger : passengers) {
			if (passenger.hasWindowPreference()) {
				passengersWithWindowPreference.add(passenger);
			}
		}
		return passengersWithWindowPreference;
	}

	private boolean isAWindowSeat(int seatNumber, int seatsInRow) {
		return (seatNumber == 0 || seatNumber == seatsInRow - 1);
	}
	
	private List<Passenger> getPassengersWithNoWindowSeatPreference(List<Passenger> passengers) {
		List<Passenger> passengersWithNoWindowPreference = new ArrayList<Passenger>();
		for (Passenger passenger : passengers) {
			if (!passenger.hasWindowPreference()) {
				passengersWithNoWindowPreference.add(passenger);
			}
		}
		return passengersWithNoWindowPreference;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<seats.length; i++) {
			if (i!=0) sb.append("\t");
			sb.append(seats[i].isOccupied() ? seats[i].getPassenger().getPassengerId() : 0);
		}	
		return sb.toString();
	}

}
