package com.matt.airplane.booking.service.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.matt.airplane.booking.service.BaseAirplaneBookingServiceTest;
import com.matt.airplane.booking.service.file.io.FileIO;
import com.matt.airplane.booking.service.model.Booking;

import static org.junit.Assert.assertEquals;

public class AirplaneBookingServiceControllerTest extends BaseAirplaneBookingServiceTest {

	private static final List<String> GROUP_SET_FITTING = Arrays.asList("1W 2 3", "4 5 6 7", "8", "9 10 11W", "12W",
			"13 14", "15 16");
	private static final List<String> GROUP_SET_NON_FITTING_ONE = Arrays.asList("1W 2 3 4", "5 6 7 8", "9", "10 11 12W",
			"13W", "14 15", "16 17");

	private static final List<String> GROUP_SET_NON_FITTING_TWO = Arrays.asList("1W 2 3", "5 6 7", "8 9 10", "11 12 13",
			"14 15W", "16", "17 18W");

	@Mock
	private FileIO mockFileAccess;

	private AirplaneBookingServiceController airplaneBookingServiceController;

	@Before
	public void setup() {
		airplaneBookingServiceController = new AirplaneBookingServiceController(mockFileAccess);
	}

	@Test
	public void givenPlaneAndPerfectlyFittingGroup_whenIRunControllerWithDecreasingOrderFirst_thenBookingIsCreatedWithEveryoneHappy()
			throws IOException {
		when(mockFileAccess.readFromFile(any(File.class)))
				.thenReturn(createLinesOfFileForPlaneAndGroups(GROUP_SET_FITTING));

		Booking booking = airplaneBookingServiceController.configureOptimalSeatingPlanDecreasingOrder("blah.txt");

		assertEquals(100.0f, booking.getPercentageOfPassengersHappy(), 0.0f);
	}

	@Test
	public void givenPlaneAndPerfectlyFittingGroup_whenIRunControllerWithBruteForce_thenBookingIsCreatedWithEveryoneHappy()
			throws IOException {
		when(mockFileAccess.readFromFile(any(File.class)))
				.thenReturn(createLinesOfFileForPlaneAndGroups(GROUP_SET_FITTING));

		Booking booking = airplaneBookingServiceController.configureOptimalSeatingPlanBruteForceWithOptimal("blah.txt");

		assertEquals(100.0f, booking.getPercentageOfPassengersHappy(), 0.0f);
	}

	@Test
	public void givenPlaneAndNonFittingGroup_whenIRunControllerWithDecreasingOrderFirst_thenBookingIsCreatedWithXPercentHappy()
			throws IOException {
		when(mockFileAccess.readFromFile(any(File.class)))
				.thenReturn(createLinesOfFileForPlaneAndGroups(GROUP_SET_NON_FITTING_ONE));

		Booking booking = airplaneBookingServiceController.configureOptimalSeatingPlanDecreasingOrder("blah.txt");

		assertEquals(94.0f, booking.getPercentageOfPassengersHappy(), 0.0f);
	}

	@Test
	public void givenPlaneAndNonFittingGroup_whenIRunControllerWithBruteForce_thenBookingIsCreatedWithXPercentHappy()
			throws IOException {
		when(mockFileAccess.readFromFile(any(File.class)))
				.thenReturn(createLinesOfFileForPlaneAndGroups(GROUP_SET_NON_FITTING_ONE));

		Booking booking = airplaneBookingServiceController.configureOptimalSeatingPlanBruteForceWithOptimal("blah.txt");

		assertEquals(94.0f, booking.getPercentageOfPassengersHappy(), 0.0f);
	}

	@Test
	public void givenPlaneAndNonFittingGroupTwo_whenIRunControllerWithDecreasingOrderFirst_thenBookingIsCreatedWithXPercentHappy() throws IOException {
		when(mockFileAccess.readFromFile(any(File.class)))
				.thenReturn(createLinesOfFileForPlaneAndGroups(GROUP_SET_NON_FITTING_TWO));

		Booking booking = airplaneBookingServiceController.configureOptimalSeatingPlanDecreasingOrder("blah.txt");
		System.out.println(booking.getPlane());
		assertEquals(76.0f, booking.getPercentageOfPassengersHappy(), 0.0f);
	}
	
	@Test
	public void givenPlaneAndNonFittingGroupTwo_whenIRunControllerWithBruteForce_thenBookingIsCreatedWithXPercentHappy() throws IOException {
		when(mockFileAccess.readFromFile(any(File.class)))
				.thenReturn(createLinesOfFileForPlaneAndGroups(GROUP_SET_NON_FITTING_TWO));

		Booking booking = airplaneBookingServiceController.configureOptimalSeatingPlanBruteForceWithOptimal("blah.txt");
		System.out.println(booking.getPlane());
		assertEquals(82.0f, booking.getPercentageOfPassengersHappy(), 0.0f);
	}

	private List<String> createLinesOfFileForPlaneAndGroups(List<String> groupsString) {
		List<String> linesOfTestFile = new ArrayList<String>();
		linesOfTestFile.add("4 4"); // Add plane dimensions
		linesOfTestFile.addAll(groupsString);
		return linesOfTestFile;
	}

}
