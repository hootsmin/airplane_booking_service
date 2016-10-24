package com.matt.airplane.booking.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.matt.airplane.booking.service.exception.PassengerException;

public class Group implements Comparable<Group>, Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private List<Passenger> passengers = new ArrayList<Passenger>();
	private final int seatsInRow;

	public Group(String groupString, int seatsInRow) {
		String[] passengersString = groupString.split(" ");
		this.seatsInRow = seatsInRow;

		for (String passengerString : passengersString) {
			try {
				Passenger passenger = new Passenger(passengerString);
				passengers.add(passenger);
			} catch (PassengerException pe) {
				// Ignore this passenger, don't add to group
			}
		}
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public int getGroupSize() {
		return passengers.size();
	}

	public int getSeatsInRow() {
		return this.seatsInRow;
	}

	public void resetHappiness() {
		for (Passenger passenger : passengers) {
			passenger.setHappy(false);
		}
	}

	public int compareTo(Group otherGroup) {
		return otherGroup.getGroupSize() - this.getGroupSize();
	}

	public int getNumberOfPassengersWithWindowPreference() {
		int numberOfPassengersWithWindowPreference = 0;
		for (Passenger passenger : passengers) {
			if (passenger.hasWindowPreference())
				numberOfPassengersWithWindowPreference++;
		}
		return numberOfPassengersWithWindowPreference;
	}

	@Override
	public String toString() {
		return "Group [passengers=" + passengers + "]";
	}

	public static Comparator<Group> GroupDescendingSizeThenByWindowSeatPreferences = new Comparator<Group>() {

		public int compare(Group group1, Group group2) {
			if (group1.getGroupSize() - group2.getGroupSize() == 0) {
				// If group size equal to number of seats in row, this is most
				// preferential
				int group1WindowSeatPreferences = group1.getNumberOfPassengersWithWindowPreference();
				int group2WindowSeatPreferences = group2.getNumberOfPassengersWithWindowPreference();

				if (groupTakesUpWholeRow(group1)) {
					if (group1WindowSeatPreferences == 2)
						return -1;
					else if (group2WindowSeatPreferences == 2)
						return 1;
					else if (group1WindowSeatPreferences == 1)
						return -1;
					else if (group2WindowSeatPreferences == 1)
						return 1;
					else
						return group1WindowSeatPreferences - group2WindowSeatPreferences;
				} else {
					if (group1WindowSeatPreferences == 1)
						return -1;
					else if (group2WindowSeatPreferences == 1)
						return 1;
					else
						return group1WindowSeatPreferences - group2WindowSeatPreferences;
				}

			} else {
				return group1.compareTo(group2);
			}
		}

		private boolean groupTakesUpWholeRow(Group group) {
			return (group.getGroupSize() == group.getSeatsInRow());
		}

	};

	public static Comparator<Group> GroupIncreasingSize = new Comparator<Group>() {

		public int compare(Group group1, Group group2) {
			return group2.compareTo(group1);
		}
	};

}
