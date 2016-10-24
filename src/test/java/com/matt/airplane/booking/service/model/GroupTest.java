package com.matt.airplane.booking.service.model;

import org.junit.Test;

import com.matt.airplane.booking.service.BaseAirplaneBookingServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupTest extends BaseAirplaneBookingServiceTest {

	private static final int ROW_SIZE_FOUR = 4;
	
	@Test
	public void givenValidGroupString_thenGroupIsCreated() {
		Group group = new Group("1 2 3W", ROW_SIZE_FOUR);
		
		assertEquals(1, group.getNumberOfPassengersWithWindowPreference());
		assertEquals(3, group.getGroupSize());
	}
	
	@Test
	public void givenInvalidGroupString_thenEmptyGroupIsCreated() {
		Group group = new Group("E F G", ROW_SIZE_FOUR);
		
		assertEquals(0, group.getNumberOfPassengersWithWindowPreference());
		assertEquals(0, group.getGroupSize());
		assertTrue(group.getPassengers().isEmpty());
	}
	
	@Test
	public void givenValidGroups_whenIOrderGroups_thenCorrectOrderingIsObserved() {
		final Group group1 = new Group("1 2 3W", ROW_SIZE_FOUR);
		final Group group2 = new Group("3 4 5 6", ROW_SIZE_FOUR);
		final Group group3 = new Group("7W 8 9W", ROW_SIZE_FOUR);
		final Group group4 = new Group("10 11", ROW_SIZE_FOUR);
		final Group group5 = new Group("12W 13 14 15", ROW_SIZE_FOUR);
		final Group group6 = new Group("16", ROW_SIZE_FOUR);
		final Group group7 = new Group("17W 18 19 20W", ROW_SIZE_FOUR);
		
		List<Group> unOrderedGroups = new ArrayList<Group>() {{
			add(group1);add(group2);add(group3);add(group4);add(group5);add(group6);add(group7);
		}};
		
		List<Group> expectedOrderedGroup = new ArrayList<Group>() {{
			add(group7);add(group5);add(group2);add(group1);add(group3);add(group4);add(group6);
		}};
		
		Collections.sort(unOrderedGroups, Group.GroupDescendingSizeThenByWindowSeatPreferences);
		
		assertEquals(expectedOrderedGroup, unOrderedGroups);
	}
}
