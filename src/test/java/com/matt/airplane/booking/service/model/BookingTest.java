package com.matt.airplane.booking.service.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.matt.airplane.booking.service.BaseAirplaneBookingServiceTest;

import static org.junit.Assert.assertEquals;

public class BookingTest extends BaseAirplaneBookingServiceTest {

	private static final List<String> GROUP_SET_FITTING = Arrays.asList("1W 2 3", "4 5 6 7", "8", "9 10 11W", "12W",
			"13 14", "15 16");
	private static final List<String> GROUP_SET_NON_FITTING = Arrays.asList("1W 2 3 4", "5 6 7 8", "9", "10 11 12W", "13W",
			"14 15", "16 17");

	@Test
	public void givenBooking_whenIAssignFittingSetOfGroupsToPlane_thenPlaneIsFullAndEveryoneIsHappy() {
		Booking booking = buildBooking(GROUP_SET_FITTING, 4, 4);
		for (Group group : booking.getGroups()) {
			booking.getPlane().addGroupToFirstFitRow(group);
		}
		
		assertEquals(100.0f, booking.getPercentageOfPassengersHappy(), 0.0f);
	}

	@Test
	public void givenBooking_whenIAssignNonFittingSetOfGroupsToPlane_thenEveryoneIsNotHappy() {
		Booking booking = buildBooking(GROUP_SET_NON_FITTING, 4, 4);
		for (Group group : booking.getGroups()) {
			booking.getPlane().addGroupToFirstFitRow(group);
		}
		
		assertEquals(88.2f, booking.getPercentageOfPassengersHappy(), 0.3f);
	}
	
	private Booking buildBooking(List<String> groupsString, int seatsInRow, int numberOfRows) {
		Booking booking = new Booking();
		booking.setGroups(buildGroups(groupsString, seatsInRow));
		booking.setPlane(buildPlane(seatsInRow, numberOfRows));
		return booking;
	}

	private List<Group> buildGroups(List<String> groupsString, int seatsInRow) {
		List<Group> groups = new ArrayList<Group>();
		for (String groupString : groupsString) {
			groups.add(buildGroup(groupString, seatsInRow));
		}
		return groups;
	}

	private Plane buildPlane(int seatsInRow, int numberOfRow) {
		return new Plane(new String(seatsInRow + " " + numberOfRow));
	}

	private Group buildGroup(String groupString, int seatsInRow) {
		return new Group(groupString, seatsInRow);
	}
}
