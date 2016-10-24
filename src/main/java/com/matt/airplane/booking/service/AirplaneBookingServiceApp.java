package com.matt.airplane.booking.service;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.matt.airplane.booking.service.controller.AirplaneBookingServiceController;
import com.matt.airplane.booking.service.exception.BookingException;
import com.matt.airplane.booking.service.model.Booking;

public class AirplaneBookingServiceApp {

	private AnnotationConfigApplicationContext applicationContext;
	private AirplaneBookingServiceController airplaneBookingServiceController;

	public AirplaneBookingServiceApp(AnnotationConfigApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		this.applicationContext.scan("com.matt.airplane.booking.service");
		this.applicationContext.refresh();
		this.airplaneBookingServiceController = applicationContext.getBean(AirplaneBookingServiceController.class);
		applicationContext.registerShutdownHook();
	}

	public static void main(String[] args) throws InterruptedException, IOException, BookingException, ParseException {
		final AirplaneBookingServiceApp airplaneBookingServiceApp = new AirplaneBookingServiceApp(
				new AnnotationConfigApplicationContext());
		Options options = new Options();
		options.addOption("i", true, "Input file");
		options.addOption("b", false, "Use brute force");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (!cmd.hasOption("i")) {
			System.out.println("Must specify input file!");
		} else {
			String inputFile = cmd.getOptionValue("i");
			boolean bruteForce = cmd.hasOption("b");

			airplaneBookingServiceApp.start(inputFile, bruteForce);
		}
	}

	public void start(String inputFile, boolean useBruteForce)
			throws InterruptedException, IOException, BookingException {
		Booking booking = new Booking();
		if (useBruteForce) {
			booking = airplaneBookingServiceController.configureOptimalSeatingPlanBruteForceWithOptimal(inputFile);
		} else {
			booking = airplaneBookingServiceController.configureOptimalSeatingPlanDecreasingOrder(inputFile);
		}
		System.out.println(booking.getPlane());
		System.out.println(booking.getPercentageOfPassengersHappy() + "%");
	}

	public AnnotationConfigApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
