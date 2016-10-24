package com.matt.airplane.booking.service.model;

import org.junit.Test;

import com.matt.airplane.booking.service.BaseAirplaneBookingServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RowTest extends BaseAirplaneBookingServiceTest {

	private static final int ROW_SIZE_FIVE = 5;
	
	@Test
	public void givenRow_whenIAttemptToAssignFittingGroupToRow_thenRowCapacityReduced() {
		Row row = new Row(ROW_SIZE_FIVE);
		Group group = new Group("1 2 3", ROW_SIZE_FIVE);
		
		assertTrue(row.addGroupToRow(group));
		assertEquals(2, row.getSeatsAvailable());
		assertEquals(group.getPassengers(), row.getPassengersAssignedToRow());
	}
	
	@Test
	public void givenRow_whenIAttemptToAssignNonFittingGroupToRow_thenRowCapacityNotReduced() {
		Row row = new Row(ROW_SIZE_FIVE);
		Group group1 = new Group("1 2 3", ROW_SIZE_FIVE);
		Group group2 = new Group("4 5 6", ROW_SIZE_FIVE);
		
		assertTrue(row.addGroupToRow(group1));
		assertFalse(row.addGroupToRow(group2));
		assertEquals(2, row.getSeatsAvailable());
		assertEquals(group1.getPassengers(), row.getPassengersAssignedToRow());
	}
	
	@Test
	public void givenRow_whenIAttemptToAssignTwoFittingGroupsToRow_thenRowCapacityReduced() {
		Row row = new Row(ROW_SIZE_FIVE);
		Group group1 = new Group("1 2 3", ROW_SIZE_FIVE);
		Group group2 = new Group("4 5", ROW_SIZE_FIVE);
		
		assertTrue(row.addGroupToRow(group1));
		assertTrue(row.addGroupToRow(group2));
		assertEquals(0, row.getSeatsAvailable());
	}
	
	@Test
	public void givenFullRow_whenIRemoveAllPassengersFromRow_thenICanAddANewGroup() {
		Row row = new Row(ROW_SIZE_FIVE);
		Group group1 = new Group("1 2 3 4 5", ROW_SIZE_FIVE);
		
		assertTrue(row.addGroupToRow(group1));
		row.removeAllPassengersFromRow();
		assertTrue(row.addGroupToRow(group1));
	}
}
