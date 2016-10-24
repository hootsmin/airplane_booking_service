package com.matt.airplane.booking.service.model;

import java.util.ArrayList;
import java.util.List;

public class Booking implements Cloneable {

	private Plane plane;
	private List<Group> groups = new ArrayList<Group>();

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group) {
		groups.add(group);
	}

	public float getPercentageOfPassengersHappy() {
		float percentagePassengersHappy = 0.0f;
		int totalPassengers = getTotalPassengersInGroups();
		if (totalPassengers > 0) {
			int passengersHappy = 0;
			for (Passenger passenger : plane.getPassengersOnPlane()) {
				if (passenger.isHappy()) {
					passengersHappy++;
				}
			}
			percentagePassengersHappy = (float) ((passengersHappy * 100) / totalPassengers);
		}
		return percentagePassengersHappy;
	}

	public void removePassengersFromPlaneAndResetPassengerHappiness() {
		plane.removeAllPassengersFromPlane();
		for (Group group : groups) {
			group.resetHappiness();
		}
	}

	public int getNumberOfPassengersWithWindowPreference() {
		int numberOfPassengersWithWindowPreference = 0;
		for (Group group : groups) {
			numberOfPassengersWithWindowPreference += group.getNumberOfPassengersWithWindowPreference();
		}
		return numberOfPassengersWithWindowPreference;
	}

	public int getTotalPassengersInGroups() {
		int totalPassengers = 0;
		for (Group group : groups) {
			totalPassengers += group.getGroupSize();
		}
		return totalPassengers;
	}

	@Override
	public String toString() {
		return "Booking [plane=" + plane + ", groups=" + groups + "]";
	}

}
