package com.matt.airplane.booking.service.model;

import org.junit.Test;

import com.matt.airplane.booking.service.BaseAirplaneBookingServiceTest;
import com.matt.airplane.booking.service.exception.PassengerException;

import static org.junit.Assert.assertEquals;

public class PassengerTest extends BaseAirplaneBookingServiceTest {

	@Test
	public void testCorrectlyFormattedPassengerStringWithNoWindowPreference() {
		Passenger passenger = new Passenger("1");
		
		assertEquals(1, passenger.getPassengerId());
		assertEquals(false, passenger.hasWindowPreference());
	}
	
	@Test
	public void testCorrectlyFormattedPassengerStringWithWindowPreference() {
		Passenger passenger = new Passenger("1W");
		
		assertEquals(1, passenger.getPassengerId());
		assertEquals(true, passenger.hasWindowPreference());
	}
	
	@Test(expected = PassengerException.class)
	public void testBadlyFormattedPassengerString() {
		new Passenger("12WW");
	}
	
	@Test
	public void givenPassenger_whenWeSetThemAsHappy_thenTheyAreHappy() {
		Passenger passenger = new Passenger("1W");
		passenger.setHappy(true);
		
		assertEquals(true, passenger.isHappy());
	}
	
	@Test
	public void givenPassenger_whenWeSetThemAsNotHappy_thenTheyAreNotHappy() {
		Passenger passenger = new Passenger("1W");
		passenger.setHappy(false);
		
		assertEquals(false, passenger.isHappy());
	}
}