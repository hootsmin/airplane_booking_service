package com.matt.airplane.booking.service.model;

import org.junit.Test;

import com.matt.airplane.booking.service.BaseAirplaneBookingServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeatTest extends BaseAirplaneBookingServiceTest {

	@Test
	public void testWindowSeat() {
		Seat seat = new Seat(true, false);
		assertTrue(seat.isWindowSeat());
	}
	
	@Test
	public void testNonWindowSeat() {
		Seat seat = new Seat(false, false);
		assertFalse(seat.isWindowSeat());
	}
	
	@Test
	public void givenUnOccupiedSeat_whenIAssignPassengerToSeat_thenSeatIsOccupied() {
		Seat seat = new Seat(false, false);
		Passenger passenger = new Passenger("1");
		
		seat.setOccupied(passenger);
		assertEquals(passenger, seat.getPassenger());
		assertTrue(seat.isOccupied());
	}
	
	@Test
	public void givenOccupiedSeat_whenIRemovePassengerFromSeat_thenSeatIsUnoccupied() {
		Seat seat = new Seat(false, false);
		Passenger passenger = new Passenger("1");
		
		seat.setOccupied(passenger);
		seat.removePassengerFromSeat();
		assertFalse(seat.isOccupied());
	}
	
	@Test
	public void givenOccupiedSeat_whenIAttemptToAssignSeat_thenSeatRemainsWithOriginalPassenger() {
		Seat seat = new Seat(false, false);
		Passenger passenger1 = new Passenger("1");
		Passenger passenger2 = new Passenger("2");
		
		seat.setOccupied(passenger1);
		assertFalse(seat.setOccupied(passenger2));
		assertEquals(passenger1, seat.getPassenger());
	}
}
