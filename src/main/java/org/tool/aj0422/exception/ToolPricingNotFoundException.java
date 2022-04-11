package org.tool.aj0422.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ToolPricingNotFoundException extends RuntimeException{
    public ToolPricingNotFoundException(String toolCode, LocalDateTime checkOutTime) {
        super("Tool pricing not found for " + toolCode + " at check out time " +
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(checkOutTime));
    }
}
