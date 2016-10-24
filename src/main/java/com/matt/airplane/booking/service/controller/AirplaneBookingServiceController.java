package com.matt.airplane.booking.service.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matt.airplane.booking.service.exception.BookingException;
import com.matt.airplane.booking.service.file.io.FileIO;
import com.matt.airplane.booking.service.model.Booking;
import com.matt.airplane.booking.service.model.Group;
import com.matt.airplane.booking.service.model.Passenger;
import com.matt.airplane.booking.service.model.Plane;
import com.matt.airplane.booking.service.model.Row;

@Component
public class AirplaneBookingServiceController {

	private final FileIO fileAccess;

	@Autowired
	public AirplaneBookingServiceController(final FileIO fileAccess) {
		this.fileAccess = fileAccess;
	}

	public Booking configureOptimalSeatingPlanBruteForceWithOptimal(String inputFile)
			throws IOException, BookingException {
		Booking booking = readLinesFromFileAndCreateBooking(inputFile);

		float optimal = calculateMaximumPotentialCustomerSatisfaction(booking);
		bruteForceWithOptimal(booking, optimal);

		return booking;
	}

	public Booking configureOptimalSeatingPlanDecreasingOrder(String inputFile) throws IOException, BookingException {
		Booking booking = readLinesFromFileAndCreateBooking(inputFile);

		// Order groups by group size decreasing then by window seat preference
		Collections.sort(booking.getGroups(), Group.GroupDescendingSizeThenByWindowSeatPreferences);
		assignToFirstGroup(booking);

		return booking;
	}

	private void bruteForceWithOptimal(Booking booking, float optimal) {
		Plane bestPlane = null;
		float bestHappy = 0.0f;
		List<Group> groupsCopy = new ArrayList<Group>(booking.getGroups());
		List<Group> bestUnAssignedGroups = new CopyOnWriteArrayList<Group>();
		boolean foundOptimal = false;

		List<List<Group>> allGroupPerms = generatePerm(groupsCopy);
		Iterator<List<Group>> allGroupPermsIter = allGroupPerms.iterator();

		while (allGroupPermsIter.hasNext() && !foundOptimal) {
			List<Group> groups = allGroupPermsIter.next();
			Plane plane = booking.getPlane();
			List<Group> unAssignedGroups = new CopyOnWriteArrayList<Group>();
			for (Group group : groups) {
				if (!plane.addGroupToFirstFitRow(group)) {
					Group groupCopy = SerializationUtils.clone(group);
					unAssignedGroups.add(groupCopy);
				}
			}

			if (booking.getPercentageOfPassengersHappy() > bestHappy) {
				bestHappy = booking.getPercentageOfPassengersHappy();
				bestPlane = SerializationUtils.clone(booking.getPlane());
				bestUnAssignedGroups = unAssignedGroups;
				if (bestHappy >= optimal) {
					foundOptimal = true;
				}
			}
			booking.removePassengersFromPlaneAndResetPassengerHappiness();
		}
		if (bestUnAssignedGroups != null) {
			attemptToAssignUnAssignedGroupsToPlane(booking, bestUnAssignedGroups);
		}
		booking.setPlane(bestPlane);
	}

	private void assignToFirstGroup(Booking booking) {
		Plane plane = booking.getPlane();
		List<Group> unAssignedGroups = new CopyOnWriteArrayList<Group>();
		for (Group group : booking.getGroups()) {
			if (!plane.addGroupToFirstFitRow(group)) {
				unAssignedGroups.add(group);
			}
		}
		attemptToAssignUnAssignedGroupsToPlane(booking, unAssignedGroups);
	}

	private void attemptToAssignUnAssignedGroupsToPlane(Booking booking, List<Group> unAssignedGroups) {
		int numberOfFreeSeats = calculateNumberOfFreeSeatsOnPlane(booking.getPlane());
		Iterator<Group> groupsIter = unAssignedGroups.iterator();
		Collections.sort(unAssignedGroups, Group.GroupIncreasingSize);
		while (numberOfFreeSeats > 0) {
			numberOfFreeSeats = calculateNumberOfFreeSeatsOnPlane(booking.getPlane());
			if (groupsIter.hasNext()) {
				// Is there enough free seats to accomodate this group?
				Group nextGroup = groupsIter.next();
				int groupSize = nextGroup.getPassengers().size();
				if (groupSize <= numberOfFreeSeats) {
					assignGroupToPlaneIndividually(nextGroup, booking);
				} else {
					numberOfFreeSeats = 0; // get out
				}
			} else {
				numberOfFreeSeats = 0; // get out
			}
		}
	}

	private void assignGroupToPlaneIndividually(Group group, Booking booking) {
		// TODO: Refector to attempt to assign bigger sub-groups to plane rather
		// than individually.
		for (Passenger passenger : group.getPassengers()) {
			booking.getPlane().addPassengerToFirstFitRow(passenger, false);
		}

	}

	private int calculateNumberOfFreeSeatsOnPlane(Plane plane) {
		int freeSeats = 0;
		List<Row> freeRows = plane.getRowsWithFreeSeatsOnPlane();
		for (Row freeRow : freeRows) {
			freeSeats += freeRow.getSeatsAvailable();
		}
		return freeSeats;
	}

	private List<List<Group>> generatePerm(List<Group> original) {
		if (original.size() == 0) {
			List<List<Group>> result = new ArrayList<List<Group>>();
			result.add(new ArrayList<Group>());
			return result;
		}
		Group firstElement = original.remove(0);
		List<List<Group>> returnValue = new ArrayList<List<Group>>();
		List<List<Group>> permutations = generatePerm(original);
		for (List<Group> smallerPermutated : permutations) {
			for (int index = 0; index <= smallerPermutated.size(); index++) {
				List<Group> temp = new ArrayList<Group>(smallerPermutated);
				temp.add(index, firstElement);
				returnValue.add(temp);
			}
		}
		return returnValue;
	}

	public Booking readLinesFromFileAndCreateBooking(String inputFile) throws IOException, BookingException {
		List<String> fileLines = fileAccess.readFromFile(new File(inputFile));
		Booking booking = new Booking();
		boolean firstLine = true;

		for (String line : fileLines) {
			if (firstLine) {
				booking.setPlane(readPlaneDimensionsAndCreatePlane(line));
				firstLine = false;
			} else {
				booking.addGroup(readGroupStringAndCreateGroup(line, booking.getPlane().getSeatsInRow()));
			}
		}

		return booking;
	}

	private float calculateMaximumPotentialCustomerSatisfaction(Booking booking) {
		int seatsInPlane = booking.getPlane().getNumberOfSeatsOnPlane();
		int seatsInPlaneRow = booking.getPlane().getSeatsInRow();
		int totalCustomers = booking.getTotalPassengersInGroups();

		int potentialHappyCustomers = totalCustomers;
		// First of all, remove those groups that are too big for a row
		for (Group group : booking.getGroups()) {
			if (group.getGroupSize() > seatsInPlaneRow) {
				potentialHappyCustomers -= group.getGroupSize();
			}
		}

		// Are we still over available number of seats
		if (potentialHappyCustomers > seatsInPlane) {
			potentialHappyCustomers = seatsInPlane;
		}

		return (float) ((potentialHappyCustomers * 100) / totalCustomers);
	}

	private Plane readPlaneDimensionsAndCreatePlane(String planeDimensionsString) throws BookingException {
		return new Plane(planeDimensionsString);
	}

	private Group readGroupStringAndCreateGroup(String groupBookingString, int seatsInRow) {
		return new Group(groupBookingString, seatsInRow);
	}

}
