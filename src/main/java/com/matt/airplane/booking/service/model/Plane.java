package com.matt.airplane.booking.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.matt.airplane.booking.service.exception.BookingException;

public class Plane implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private Row[] rows;
	private int seatsInRow;

	public Plane(String planeBuildingString) {
		String[] planeDimensions = planeBuildingString.split(" ");
		if (planeDimensions.length == 2) {
			try {
				int numberOfSeatsInARow = Integer.parseInt(planeDimensions[0]);
				int numberOfRowsInPlane = Integer.parseInt(planeDimensions[1]);

				buildPlane(numberOfRowsInPlane, numberOfSeatsInARow);
			} catch (NumberFormatException nfe) {
				throw new BookingException("Plane dimension string is badly formatted: " + planeBuildingString);
			}
		} else {
			throw new BookingException("Plane dimension string is badly formatted: " + planeBuildingString);
		}
	}

	public Plane(Plane plane) {
		this.seatsInRow = plane.seatsInRow;
		this.rows = new Row[this.seatsInRow];
		for (int i = 0; i < plane.rows.length; i++) {
			this.rows[i] = plane.rows[i];
		}
	}

	private void buildPlane(int numberOfRows, int seatsInRow) {
		rows = new Row[numberOfRows];
		this.seatsInRow = seatsInRow;

		for (int i = 0; i < numberOfRows; i++) {
			Row row = new Row(seatsInRow);
			rows[i] = row;
		}
	}

	public Row[] getRows() {
		return rows;
	}

	public int getSeatsInRow() {
		return seatsInRow;
	}

	public List<Passenger> getPassengersOnPlane() {
		List<Passenger> passengers = new ArrayList<Passenger>();
		for (int i = 0; i < rows.length; i++) {
			passengers.addAll(rows[i].getPassengersAssignedToRow());
		}
		return passengers;
	}

	public boolean addGroupToFirstFitRow(Group group) {
		int groupSize = group.getGroupSize();
		int numberOfRows = rows.length;
		int currentRow = 0;
		boolean fittingRowFound = false;
		do {
			int rowSeatsRemaining = rows[currentRow].getSeatsAvailable();
			if (rowSeatsRemaining >= groupSize) {
				fittingRowFound = true;
				rows[currentRow].addGroupToRow(group);
			}
			currentRow++;
		} while (!fittingRowFound && currentRow < numberOfRows);
		return fittingRowFound;
	}

	public boolean addPassengerToFirstFitRow(Passenger passenger, boolean preferencesMet) {
		int numberOfRows = rows.length;
		int currentRow = 0;
		boolean fittingRowFound = false;
		do {
			int rowSeatsRemaining = rows[currentRow].getSeatsAvailable();
			if (rowSeatsRemaining > 0) {
				fittingRowFound = true;
				rows[currentRow].addPassengerToARow(passenger, preferencesMet);
			}
			currentRow++;
		} while (!fittingRowFound && currentRow < numberOfRows);
		return fittingRowFound;
	}

	public void removeAllPassengersFromPlane() {
		for (int i = 0; i < rows.length; i++) {
			rows[i].removeAllPassengersFromRow();
		}
	}

	public int getNumberOfSeatsOnPlane() {
		return rows.length * seatsInRow;
	}

	public List<Row> getRowsWithFreeSeatsOnPlane() {
		List<Row> rowsWithSeatsAvailable = new ArrayList<Row>();
		for (int i = 0; i < rows.length; i++) {
			if (rows[i].getSeatsAvailable() > 0) {
				rowsWithSeatsAvailable.add(rows[i]);
			}
		}
		return rowsWithSeatsAvailable;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rows.length; i++) {
			sb.append(rows[i].toString());
			if (i != rows.length - 1)
				sb.append("\n");
		}

		return sb.toString();
	}

}
