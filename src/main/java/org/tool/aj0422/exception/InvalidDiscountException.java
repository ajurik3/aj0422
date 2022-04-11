package org.tool.aj0422.exception;

public class InvalidDiscountException extends RuntimeException{
    public InvalidDiscountException (double discount) {
        super("Invalid discount of " +
                String.format("%,.2f", (discount * 100)) + "%. Discount must be between 0 and 100 percent.");
    }
}
