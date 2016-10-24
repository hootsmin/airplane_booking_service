package com.matt.airplane.booking.service.model;

import org.junit.Test;

import com.matt.airplane.booking.service.BaseAirplaneBookingServiceTest;
import com.matt.airplane.booking.service.exception.BookingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlaneTest extends BaseAirplaneBookingServiceTest {

	@Test
	public void givenCorrectlyFormatedPlaneBuildingString_thenPlaneIsCreated() {
		Plane plane = new Plane("4 4");
		assertEquals(16, plane.getNumberOfSeatsOnPlane());
		assertEquals(4, plane.getSeatsInRow());
		assertEquals(4, plane.getRows().length);
	}
	
	@Test(expected = BookingException.class)
	public void givenInCorrectlyFormattedPlaneBuildingString_thenExceptionIsThrown() {
		new Plane("W O");
	}
	
	@Test(expected = BookingException.class)
	public void givenPlaneBuildingStringWithTooManyDimensions_thenExceptionIsThrown() {
		new Plane("1 2 3");
	}
	
	@Test
	public void givenPlane_whenIAttemptToAssignFittingGroupToPlane_thenGroupIsAddedToPlane() {
		Plane plane = new Plane("4 4");
		Group group1 = new Group("1 2 3", 4);
		
		assertTrue(plane.addGroupToFirstFitRow(group1));
		assertEquals(3, plane.getPassengersOnPlane().size());
	}
	
	@Test
	public void givenPlane_whenIAttemptToAssignGroupWhichIsTooBigToFitInRow_thenGroupIsNotAddedToPlane() {
		Plane plane = new Plane("4 4");
		Group group1 = new Group("1 2 3 4 5", 4);
		
		assertFalse(plane.addGroupToFirstFitRow(group1));
		assertEquals(0, plane.getPassengersOnPlane().size());
	}
	
	@Test
	public void givenPlane_whenIAttemptToNonFittingGroup_thenGroupIsNotAddedToPlane() {
		Plane plane = new Plane("2 2");
		Group group1 = new Group("1 2", 2);
		Group group2 = new Group("3 4", 2);
		Group group3 = new Group("5", 2);
		
		assertTrue(plane.addGroupToFirstFitRow(group1));
		assertTrue(plane.addGroupToFirstFitRow(group2));
		assertFalse(plane.addGroupToFirstFitRow(group3));
		assertEquals(4, plane.getPassengersOnPlane().size());
	}
	
	@Test
	public void givenFullPlane_whenIRemoveAllPassengers_thenICanAddAGroupToPlane() {
		Plane plane = new Plane("2 2");
		Group group1 = new Group("1 2", 2);
		Group group2 = new Group("3 4", 2);
		Group group3 = new Group("5", 2);
		
		assertTrue(plane.addGroupToFirstFitRow(group1));
		assertTrue(plane.addGroupToFirstFitRow(group2));
		assertFalse(plane.addGroupToFirstFitRow(group3));
		plane.removeAllPassengersFromPlane();
		assertTrue(plane.addGroupToFirstFitRow(group1));
	}
}
