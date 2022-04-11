package org.tool.aj0422.exception;

public class InvalidRentalDaysException extends RuntimeException {
    public InvalidRentalDaysException (int rentalDays) {
        super("Invalid number of rental days: " + rentalDays + ". Number of rental days must be greater than 0.");
    }
}
